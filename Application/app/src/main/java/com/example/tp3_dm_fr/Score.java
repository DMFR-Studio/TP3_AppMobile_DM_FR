package com.example.tp3_dm_fr;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;
@DatabaseTable( tableName = "T_Scores" )
public class Score {
    @DatabaseField( columnName = "idScore", generatedId = true )
    private int idScore;
    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "idUser", foreignAutoCreate = true )
    private User user;
    @DatabaseField
    private int score;
    @DatabaseField
    private Date when;

    public Score() {
    }

    public Score(User user, int score, Date when) {
        this.user = user;
        this.score = score;
        this.when = when;
    }

    public int getIdScore() {
        return idScore;
    }

    public void setIdScore(int idScore) {
        this.idScore = idScore;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    @Override
    public String toString() {
        return idScore + ": " + user + " -> " + score + " : " + when.toString();
    }
}
