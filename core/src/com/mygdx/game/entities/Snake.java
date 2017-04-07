package com.mygdx.game.entities;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.configs.GameConfig;

/**
 * Created by Jay Nguyen on 4/4/2017.
 */

public class Snake {

    private static final Logger log = new Logger(Snake.class.getName(), Logger.DEBUG);

    private final Array<BodyPart> bodyParts = new Array<BodyPart>();

    private SnakeHead head;
    private Direction direction = Direction.RIGHT; //default direction

    private float xBeforeUpdate;
    private float yBeforeUpdate;

    public Snake() {
        head = new SnakeHead();
    }

    public void move() {

        xBeforeUpdate = head.getX(); //get head's last position because it's called before snake moves
        yBeforeUpdate = head.getY();

        if (direction.isRight()) {
            head.updateX(GameConfig.SNAKE_SPEED);
        } else if (direction.isLeft()) {
            head.updateX(-GameConfig.SNAKE_SPEED);
        } else if (direction.isUp()) {
            head.updateY(GameConfig.SNAKE_SPEED);
        } else if (direction.isDown()) {
            head.updateY(-GameConfig.SNAKE_SPEED);
        }

        updateBodyParts(); //update after the snake moves
    }


    public void insertBodyPart() { //add/insert new body part(s)
        BodyPart bodyPart = new BodyPart();
        bodyPart.setPosition(head.getX(), head.getY()); //set new body part's position to head's position
        bodyParts.insert(0, bodyPart); //move tail behind head
    }

    private void updateBodyParts() {
        if (bodyParts.size > 0) { //check if there is already at least 1 body part
            BodyPart tail = bodyParts.removeIndex(0); //remove or it will keep inserting bodyparts
            tail.setPosition(xBeforeUpdate, yBeforeUpdate); //set tail's position to last position of head
            bodyParts.add(tail); //add tail back to body parts array
        }
    }

    public void resetBodyParts() {
        bodyParts.clear();
        direction = Direction.RIGHT; //default direction
        head.setPosition(0, 0); //default position
    }

    //direction to snake's direction
    public void setDirection(Direction newDirection) {
        //check if the direction is not the opposite direction or snake body part size is 0, then allow snake to change direction
        if(!direction.isOppositeDirection(newDirection) || bodyParts.size == 0)
        direction = newDirection;
    }

    public SnakeHead getHead() {
        return head;
    }

    public Array<BodyPart> getBodyParts() {
        return bodyParts;
    }


}
