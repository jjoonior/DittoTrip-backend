package site.dittotrip.ditto_trip.spot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;
import site.dittotrip.ditto_trip.hashtag.domain.entity.SpotHashtag;
import site.dittotrip.ditto_trip.image.domain.Image;
import site.dittotrip.ditto_trip.spot.categoryspot.domain.CategorySpot;
import site.dittotrip.ditto_trip.spot.stillcut.domain.StillCut;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Spot {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id")
    private Long id;

    private String spotName;
    private String intro;
    private String address;
    private LocalTime startTime;
    private LocalTime endTime;
    private String phoneNumber;
    private String homeUri;
    private Point point;

    @OneToMany(mappedBy = "spot")
    private List<CategorySpot> categorySpots;

    @OneToOne(mappedBy = "spot")
    private Image image;

    @OneToMany(mappedBy = "spot")
    private List<StillCut> stillCuts = new ArrayList<>();

    @OneToMany(mappedBy = "spot")
    private List<SpotHashtag> spotHashtags = new ArrayList<>();

}
