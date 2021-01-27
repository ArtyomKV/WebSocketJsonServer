package com.realtrac.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "way_point")
public class WayPoint {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "way_point_seq")
    @Column(name = "way_point_id")
    private Long wayPointId;

    private double x;
    private double y;
    private double velocity;
    private int delayMillis;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "hero_id")
    private Hero hero;
}
