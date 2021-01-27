package com.realtrac.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hero")
public class Hero {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hero_seq")
    @Column(name = "hero_id")
    private Long heroId;

    private String name;
    private String house;

    @OneToMany(mappedBy = "hero", fetch = FetchType.EAGER)
    List<WayPoint> wayPoints;
}
