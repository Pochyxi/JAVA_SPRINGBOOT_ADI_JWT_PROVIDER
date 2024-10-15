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

}
