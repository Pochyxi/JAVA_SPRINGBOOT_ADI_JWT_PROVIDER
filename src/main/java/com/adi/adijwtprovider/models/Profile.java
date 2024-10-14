package com.adi.adijwtprovider.models;

import com.adi.adijwtprovider.enums.ProfileList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profile {
    private Long id;

    private User user;

    private ProfileList name;

    private int power;

    private Set<ProfilePermission> profilePermissions;


    public Profile( ProfileList profileName ) {

        this.name = profileName;

        switch (this.name) {

            case ADMIN -> this.power = 0;

            case USER -> this.power = 10;

            default -> this.power = 100;
        }

    }

}
