package com.adi.adijwtprovider.models;

import com.adi.adijwtprovider.enums.PermissionList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    private Long id;

    private PermissionList name;

    private Set<ProfilePermission> profilePermissions;

}
