package de.jbg.memeapp.Memes;
import lombok.Getter;
import lombok.Setter;

import java.sql.Blob;
import java.util.Date;

@Getter
@Setter
public class Meme { //DTO

    private Integer meme_ID;
    private Blob pic;
    private Date date;
    private Integer height;
    private Integer length;
    private Integer size;

}