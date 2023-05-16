package com.example.firebase.util;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class User {

    private String id;
    private String name;
    private String login;
}
