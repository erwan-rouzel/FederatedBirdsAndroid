package fr.sio.ecp.federatedbirds.model;

import java.util.HashSet;

/**
 * Created by Michaël on 24/11/2015.
 */
public class User {

    public long id;
    public String login;
    public String avatar;
    public String coverPicture;
    public String email;
    public String password;
    public HashSet<Long> followedBy = new HashSet<>();
    public HashSet<Long> followerOf = new HashSet<>();
}
