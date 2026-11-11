package lab.entities;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Player.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Player_ {

	public static final String SCORES = "scores";
	public static final String NICKNAME = "nickname";
	public static final String ID = "id";
	public static final String GAME_SAVES = "gameSaves";

	
	/**
	 * @see lab.entities.Player#scores
	 **/
	public static volatile ListAttribute<Player, Score> scores;
	
	/**
	 * @see lab.entities.Player#nickname
	 **/
	public static volatile SingularAttribute<Player, String> nickname;
	
	/**
	 * @see lab.entities.Player#id
	 **/
	public static volatile SingularAttribute<Player, Long> id;
	
	/**
	 * @see lab.entities.Player
	 **/
	public static volatile EntityType<Player> class_;
	
	/**
	 * @see lab.entities.Player#gameSaves
	 **/
	public static volatile ListAttribute<Player, GameSave> gameSaves;

}

