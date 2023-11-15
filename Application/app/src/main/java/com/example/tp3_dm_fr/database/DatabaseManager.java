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

    /**
     * Insère un score dans la base de données.
     *
     * @param score L'objet Score à insérer dans la base de données.
     */
    public void insertScore( Score score ) {
        try {
            Dao<Score, Integer> dao = getDao( Score.class );
            dao.create( score );
            Log.i( "DATABASE", "insertScore invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert score into Database", exception );
        }
    }


    /**
     * Lit tous les scores enregistrés dans la base de données.
     *
     * @return Une liste des scores récupérés depuis la base de données, ou null en cas d'échec.
     */
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

    /**
     * Insère un user dans la base de données.
     *
     * @param user L'objet User à insérer dans la base de données.
     */
    public void insertUser( User user ) {
        try {
            Dao<User, Integer> dao = getDao( User.class );
            dao.create( user );
            Log.i( "DATABASE", "insertUser invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't insert user into Database", exception );
        }
    }

    /**
     * Récupère le score associé à un utilisateur dans la base de données.
     *
     * @param userId L'identifiant de l'utilisateur pour lequel récupérer le score.
     * @return Le score associé à l'utilisateur spécifié.
     */
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

    /**
     * Met à jour le score et la date associés à un utilisateur dans la base de données.
     *
     * @param user L'objet User à mettre à jour avec le nouveau score.
     * @param score L'objet Score à mettre à jour avec le nouveau score et la nouvelle date.
     * @param newScore Le nouveau score à assigner à l'utilisateur.
     * @param newDate La nouvelle date à assigner au score.
     */
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

    /**
     * Récupère un utilisateur à partir de son identifiant dans la base de données.
     *
     * @param userId L'identifiant de l'utilisateur à récupérer.
     * @return L'objet User associé à l'identifiant spécifié.
     */
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

    /**
     * Récupère un utilisateur à partir de son adresse e-mail et de son mot de passe dans la base de données.
     *
     *
     * @param email L'adresse e-mail de l'utilisateur à récupérer.
     * @param password Le mot de passe de l'utilisateur à récupérer.
     * @return L'objet User associé à l'adresse e-mail et au mot de passe spécifiés
     */
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

    /**
     * Met à jour le score de l'utilisateur dans la base de données.
     *
     * @param user L'objet User dont le score doit être mis à jour.
     * @param newScore Le nouveau score à assigner à l'utilisateur.
     */
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
