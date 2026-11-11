package lab.entities;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Score.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Score_ {

	public static final String ID = "id";
	public static final String SCORE_POINTS = "scorePoints";
	public static final String PLAYER = "player";

	
	/**
	 * @see lab.entities.Score#id
	 **/
	public static volatile SingularAttribute<Score, Long> id;
	
	/**
	 * @see lab.entities.Score
	 **/
	public static volatile EntityType<Score> class_;
	
	/**
	 * @see lab.entities.Score#scorePoints
	 **/
	public static volatile SingularAttribute<Score, Integer> scorePoints;
	
	/**
	 * @see lab.entities.Score#player
	 **/
	public static volatile SingularAttribute<Score, Player> player;

}

