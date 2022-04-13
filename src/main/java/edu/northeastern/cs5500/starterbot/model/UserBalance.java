package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class UserBalance implements Model {
    ObjectId id;

    String discordUserId;

    // note that the balance could only be integer and be within the range of Integer.
    Integer balance;
}
