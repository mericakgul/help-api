package com.mericakgul.helpapi.model.entity;

import com.mericakgul.helpapi.enums.SkillType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "skill")
@Getter
@Setter
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private SkillType skillType;
}
