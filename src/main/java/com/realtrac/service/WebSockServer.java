package com.realtrac.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtrac.entities.Hero;
import com.realtrac.entities.WayPoint;
import com.realtrac.models.LocationPackage;
import com.realtrac.repositories.HeroRepositories;
import com.realtrac.repositories.WayPointRepositories;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@Slf4j
@Service
@NoArgsConstructor
public class WebSockServer implements ApplicationRunner {

    HeroRepositories heroRepositories;
    WayPointRepositories wayPointRepositories;

    private static final String URL = "ws://localhost:8080"; // отправлять на "ws://localhost:8080/" тоже пробовал
    private int i = 0;
    private List<Hero> heroes = new ArrayList<>();
    private List<List<WayPoint>> pointList = new ArrayList<>();
    private WebsocketClientEndpoint clientEndpoint;

    @Autowired
    public WebSockServer(HeroRepositories heroRepositories, WayPointRepositories wayPointRepositories) {
        this.heroRepositories = heroRepositories;
        this.wayPointRepositories = wayPointRepositories;
    }

    @PostConstruct
    public void postConstruct() {
        File dir = new File("src/main/resources/heroes"); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        List<File> lst = Arrays.asList(arrFiles);
        System.out.println(lst);

        ObjectMapper mapper = new ObjectMapper();

        for (File file : lst) {
            try {
                Hero hero = mapper.readValue(file, Hero.class);
                heroes.add(hero);
                Hero savedHero = heroRepositories.save(hero);
                List<WayPoint> wayPoints = hero.getWayPoints();
                pointList.add(wayPoints);
                for (WayPoint point : wayPoints) {
                    point.setHero(savedHero);
                    wayPointRepositories.save(point);
                }
            } catch (IOException e) {
                log.error("Ошибка доступа к файлу: {}", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("I am starting...");

        for (Hero hero : heroes) {
            log.info("Name: {}", hero.getName());
        }

////        открываю websocket
//        clientEndpoint = new WebsocketClientEndpoint(new URI(URL));
////        добавляю слушателя
//        clientEndpoint.addMessageHandler(message -> log.info("{}", message));

    }

    @Scheduled(fixedDelay = 1000)
    private void sendCoordinates() throws JsonProcessingException {
        System.out.println(i);
        ObjectMapper mapper = new ObjectMapper();

        for (Hero hero : heroes) {
            String name = hero.getName();
            String house = hero.getHouse();
            List<WayPoint> pointList = hero.getWayPoints();
            if (i < pointList.size()) {
                WayPoint wayPoint = pointList.get(i);
                LocationPackage locationPackage = new LocationPackage();
                locationPackage.setHero(name);
                locationPackage.setHouse(house);
                locationPackage.setX(wayPoint.getX());
                locationPackage.setY(wayPoint.getY());
                String json = mapper.writeValueAsString(locationPackage);
                System.out.println("Пакет: " + json);
                //отправляю локацию клиенту по websocket
//                clientEndpoint.sendMessage(json);
            }

        }

        i++;
    }


}
