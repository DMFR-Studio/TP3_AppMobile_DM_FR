package com.example.tp3_dm_fr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.Date;
import java.util.List;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "Game.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseManager(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable( connectionSource, Score.class );
            TableUtils.createTable( connectionSource, User.class );
            Log.i( "DATABASE", "onCreate invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't create Database", exception );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable( connectionSource, Score.class, true );
            TableUtils.dropTable( connectionSource, User.class, true );
            onCreate( database, connectionSource);
            Log.i( "DATABASE", "onUpgrade invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't upgrade Database", exception );
        }
    }

    public void insertScore( Score score ) {
        try {
            Dao<Score, Integer> dao = getDao( Score.class );
            dao.create( score );
            Log.i( "DATABASE", "insertScore invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert score into Database", exception );
        }
    }


    public List<Score> readScores() {
        try {
            Dao<Score, Integer> dao = getDao( Score.class );
            List<Score> scores = dao.queryForAll();
            Log.i( "DATABASE", "readScores invoked" );
            return scores;
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert score into Database", exception );
            return null;
        }
    }

    public void insertUser( User user ) {
        try {
            Dao<User, Integer> dao = getDao( User.class );
            dao.create( user );
            Log.i( "DATABASE", "insertUser invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert user into Database", exception );
        }
    }

    public Score getScoreByUserId(int userId) {
        try {
            Dao<Score, Integer> dao = getDao(Score.class);
            Score score = dao.queryForId(userId);
            if (score != null) {
                Log.i("DATABASE", "getScoreByUserId invoked");
            } else {
                Log.i("DATABASE", "Score not found in database");
            }
            return score;
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't get score into Database", exception);
            return null;
        }
    }

    public void updateScore(User user, Score score, int newScore, Date newDate) {
        try {
            user.setScore(newScore);
            Dao<User, Integer> daoUser = getDao(User.class);
            daoUser.update(user);

            score.setScore(user.getScore());
            score.setWhen(newDate);
            Dao<Score, Integer> daoScore = getDao(Score.class);
            daoScore.update(score);

            Log.i("DATABASE", "User & Score updated");
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't update user & score in Database", exception);
        }
    }



    public User getUserById(int userId) {
        try {
            Dao<User, Integer> dao = getDao(User.class);
            User user = dao.queryForId(userId);
            if (user != null) {
                Log.i("DATABASE", "getUser invoked");
            } else {
                Log.i("DATABASE", "User not found in database");
            }
            return user;
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't insert user into Database", exception);
            return null;
        }
    }

    public User getUserByEmailAndPassword(String email, String password) {
        try {
            Dao<User, Integer> dao = getDao(User.class);
            QueryBuilder<User, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("email", email).and().eq("password", password);
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
            User user = dao.queryForFirst(preparedQuery);
            if (user != null) {
                Log.i("DATABASE", "getUser invoked");
            } else {
                Log.i("DATABASE", "User not found in database");
            }
            return user;
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't insert user into Database", exception);
            return null;
        }
    }

    public void updateUserScore(User user, int newScore) {
        try {
            Dao<User, Integer> dao = getDao(User.class);
            user.setScore(newScore); //
            dao.update(user);
            Log.i("DATABASE", "User score updated");
        } catch (Exception exception) {
            Log.e("DATABASE", "Can't update user score in Database", exception);
        }
    }

}
