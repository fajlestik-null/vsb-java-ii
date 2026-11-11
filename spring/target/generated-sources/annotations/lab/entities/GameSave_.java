package lab.entities;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@StaticMetamodel(GameSave.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class GameSave_ {

	public static final String LIVES = "lives";
	public static final String CURRENT_SCORE = "currentScore";
	public static final String DESCRIPTION = "description";
	public static final String DIFFICULTY_TIMER = "difficultyTimer";
	public static final String POSITION_X = "positionX";
	public static final String POSITION_Y = "positionY";
	public static final String TRIPLE_SHOT_TIMER = "tripleShotTimer";
	public static final String RAPID_FIRE_TIMER = "rapidFireTimer";
	public static final String ASTEROID_SPAWN_INTERVAL = "asteroidSpawnInterval";
	public static final String SAVED_AT = "savedAt";
	public static final String ID = "id";
	public static final String HAS_SHIELD = "hasShield";
	public static final String DOUBLE_POINT_TIMER = "doublePointTimer";
	public static final String PLAYER = "player";

	
	/**
	 * @see lab.entities.GameSave#lives
	 **/
	public static volatile SingularAttribute<GameSave, Integer> lives;
	
	/**
	 * @see lab.entities.GameSave#currentScore
	 **/
	public static volatile SingularAttribute<GameSave, Integer> currentScore;
	
	/**
	 * @see lab.entities.GameSave#description
	 **/
	public static volatile SingularAttribute<GameSave, String> description;
	
	/**
	 * @see lab.entities.GameSave#difficultyTimer
	 **/
	public static volatile SingularAttribute<GameSave, Double> difficultyTimer;
	
	/**
	 * @see lab.entities.GameSave#positionX
	 **/
	public static volatile SingularAttribute<GameSave, Double> positionX;
	
	/**
	 * @see lab.entities.GameSave#positionY
	 **/
	public static volatile SingularAttribute<GameSave, Double> positionY;
	
	/**
	 * @see lab.entities.GameSave#tripleShotTimer
	 **/
	public static volatile SingularAttribute<GameSave, Double> tripleShotTimer;
	
	/**
	 * @see lab.entities.GameSave#rapidFireTimer
	 **/
	public static volatile SingularAttribute<GameSave, Double> rapidFireTimer;
	
	/**
	 * @see lab.entities.GameSave#asteroidSpawnInterval
	 **/
	public static volatile SingularAttribute<GameSave, Double> asteroidSpawnInterval;
	
	/**
	 * @see lab.entities.GameSave#savedAt
	 **/
	public static volatile SingularAttribute<GameSave, LocalDateTime> savedAt;
	
	/**
	 * @see lab.entities.GameSave#id
	 **/
	public static volatile SingularAttribute<GameSave, Long> id;
	
	/**
	 * @see lab.entities.GameSave#hasShield
	 **/
	public static volatile SingularAttribute<GameSave, Boolean> hasShield;
	
	/**
	 * @see lab.entities.GameSave
	 **/
	public static volatile EntityType<GameSave> class_;
	
	/**
	 * @see lab.entities.GameSave#doublePointTimer
	 **/
	public static volatile SingularAttribute<GameSave, Double> doublePointTimer;
	
	/**
	 * @see lab.entities.GameSave#player
	 **/
	public static volatile SingularAttribute<GameSave, Player> player;

}

