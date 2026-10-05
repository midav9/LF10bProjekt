# LF10bProjekt
# Wiederanlaufplan: Meme-DB mit Backup und Monitoring

Zweck: Projekt nachbauen oder nach längerer Pause wieder aufnehmen.

## 1. Überblick
- System 1 (Produktiv, Raspi): Java Frontend + Backend, MariaDB (memedb), Ansible, rsync + systemd (trigger)
- System 2 (Backup + Monitoring, Laptop): Backup-DB-Dump, health.log, ELK-Stack (mit system agent (elastic agent))
- Ablauf: systemd -> Dump erstellen -> rsync auf System 2 -> health.log schreiben -> ELK liest health.log <- agent stellt systemdaten bereit
- Architektur: siehe draw.io (Architekturuebersicht_drawio.png)
- Ziel-RTO: 30-60 min

<img width="1046" height="441" alt="261004_architecture drawio" src="https://github.com/user-attachments/assets/13c4c253-8a66-46b1-94f0-517b32407e88" />


## 2. Voraussetzungen
- 2 Linux-Systeme im selben Netzwerk (feste IPs oder Hostnamen notieren)
- Je ein Benutzer mit sudo-Rechten
- Pakete System 1: `openjdk-17-jre`, `mariadb-server`, `rsync`, `ansible`, `openssh-client`
- Pakete System 2: `rsync`, `openssh-server`, `docker` + `docker-compose` (ELK (Elasticsearch, Logstash/Filebeat, Kibana)), `mariadb-server` (nur für Restore-Tests), `elastic agent`
- Zugangsdaten (DB-User, Passwörter, ssh-keys) NICHT ins Repo, sondern lokal notieren

| Angabe | Wert |
|---|---|
| IP/Host System 1 | 192.168.1.217 |
| IP/Host System 2 | 192.168.1.131 |
| DB-Name / DB-User | memedb / root |
| Backup-Zielordner (System 2) | /srv/backup/memedb |
| Log-Pfad (System 2) | /srv/backup/health.log |

## 3. Aufbau Schritt für Schritt

### 3.1 System 1: Datenbank
1. `sudo apt install mariadb-server`
2. `sudo mariadb-secure-installation`
3. DB und User anlegen (`CREATE DATABASE memedb; CREATE USER ...; GRANT ...;`)
4. Tabellen anlegen (Schema-Datei im Repo: `schema.sql`)

### 3.2 System 1: Java-Anwendung
1. Repo klonen: `git clone <repo-url>`
2. DB-Verbindung in der Config eintragen (JDBC-URL, User, Passwort)
3. Bauen und starten (`mvn package` bzw. `java -jar ...`)
4. Test: Meme anlegen, lesen, ändern (CRU)

Beschreibung der Anwendung:
Ein kleines **Java-Maven-Projekt** zu Lernzwecken um auf eine Datenbank zuzugreifen und sich Bilder ausgeben zu lassen.  
Man soll sich im ersten Schritt ...
- **nächstes Bild anzeigen**
- **vorheriges Bild anzeigen**
- **zufälliges Bild anzeigen**

... lassen können

Aufteilung in UI (Swing) und Backend (API). Die Bilder sind als Blobs hinterlegt.

### 3.3 SSH-Verbindung System 1 -> System 2
1. Auf System 1: `ssh-keygen -t ed25519`
2. `ssh-copy-id user@system2`
3. Test: `ssh user@system2` ohne Passwort
4. Zielordner auf System 2 anlegen: `mkdir -p /srv/backup/memedb`

### 3.4 Backup-Skript (System 1): `backup.sh`
1. Dump erstellen (konsistent, statt rohe DB-Dateien zu kopieren):
   `mariadb-dump --single-transaction memedb > /tmp/memedb_$(date +%F_%H%M).sql`
2. Dump per rsync übertragen:
   `rsync -az /tmp/memedb_*.sql user@system2:/srv/backup/memedb/`
3. Ergebnis prüfen (`$?` = 0) und in health.log schreiben, z. B.:
   `2026-09-28T12:00:00 BACKUP OK memedb_2026-09-28_1200.sql 1.2MB`
   bei Fehler: `... BACKUP FAILED <Grund>`
4. health.log per rsync/ssh nach System 2 übertragen
5. Temporäre Dumps lokal löschen
6. Skript ausführbar machen: `chmod +x backup.sh`, einmal manuell testen

### 3.5 Automatisierung mit systemd (System 1)
1. `crontab -e`
2. Beispiel (täglich 02:00): `0 2 * * * /home/user/backup.sh >> /var/log/backup_cron.log 2>&1`
3. Nach dem ersten Lauf health.log auf System 2 prüfen

### 3.6 Monitoring: ELK (System 2)
1. Elasticsearch, Kibana und Filebeat/Logstash installieren (Doku: elastic.co/guide)
2. Filebeat/Logstash auf `/srv/backup/health.log` zeigen lassen
3. Kibana öffnen (Port 5601), Index/Data View anlegen
4. Dashboard (optional): letzte Backups, Fehleranzahl, Zeit seit letztem erfolgreichen Backup
5. Hinweis: ELK braucht viel RAM. Bei Raspi-Problemen: Heap-Größe reduzieren oder Alternative (Grafana + Loki)

## 4. Wiederherstellung (Restore, Ziel 30-60 min)
1. Ausfall feststellen (kein neuer OK-Eintrag im health.log / Kibana)
2. Ersatzsystem vorbereiten (Linux, `mariadb-server`, Java)
3. Neuesten Dump aus `/srv/backup/memedb/` holen
4. DB anlegen und einspielen: `mariadb memedb < memedb_<datum>.sql`
5. Java-App mit neuer DB-Adresse konfigurieren und starten
6. Funktionstest: Memes lesen, neues Meme anlegen
7. cron-Job und Monitoring auf dem neuen System wieder aktivieren

## 5. Checkliste nach längerer Pause
- [ ] Beide Systeme starten, Netzwerk und IPs prüfen
- [ ] SSH-Login ohne Passwort funktioniert
- [ ] MariaDB läuft (`systemctl status mariadb`)
- [ ] Java-App startet, DB-Verbindung ok
- [ ] `crontab -l` zeigt den Job noch an
- [ ] Letzter Eintrag im health.log ist aktuell und "OK"
- [ ] ELK/Kibana erreichbar
- [ ] Freier Speicherplatz auf System 2 ausreichend
- [ ] Alte Dumps aufräumen (z. B. älter als 30 Tage)
- [ ] Restore-Test einmal durchführen

## 6. Typische Fehler
| Problem | Ursache / Lösung |
|---|---|
| rsync fragt nach Passwort | SSH-Key nicht eingerichtet (3.3) |
| cron läuft nicht | Pfade im Skript absolut angeben, Log prüfen, Skript ausführbar? |
| Dump leer | DB-Zugangsdaten falsch, `~/.my.cnf` für den User anlegen |
| Kibana zeigt nichts | Filebeat-Pfad falsch oder Leserechte auf health.log fehlen |
| ELK startet nicht auf Raspi | Zu wenig RAM, Heap reduzieren |

## 7. Ressourcen
- MariaDB Knowledge Base (Backup, mariadb-dump)
- `man rsync`, `man 5 crontab`, crontab.guru
- Elastic-Doku (Elasticsearch, Kibana, Filebeat)
- Optional: Ansible-Doku, um das Setup später zu automatisieren

## 8. Team
- Person A: Java-App, DB-Anbindung, CRUD
- Person B: Backup-Skript, systemd, health.log, ELK
- Gemeinsam: Umgebung, Doku/GitHub, Präsentation, Debugging
