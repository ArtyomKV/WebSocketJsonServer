package com.realtrac.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtrac.endpoints.Endpoint;
import com.realtrac.entities.Hero;
import com.realtrac.entities.WayPoint;
import com.realtrac.models.LocationPackage;
import com.realtrac.repositories.HeroRepositories;
import com.realtrac.repositories.WayPointRepositories;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.tyrus.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.websocket.Session;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@NoArgsConstructor
@Setter
public class WebSockServer implements ApplicationRunner {

    HeroRepositories heroRepositories;
    WayPointRepositories wayPointRepositories;

    public static Session session = null;
    Endpoint endpoint = null;
    private int i = 0;
    private List<Hero> heroes = new ArrayList<>();

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
//                Hero savedHero = heroRepositories.save(hero);  //раскомментировать при первом запуске (сохранение данных о героях в БД)
                List<WayPoint> wayPoints = hero.getWayPoints();
//                for (WayPoint point : wayPoints) {  //раскомментировать при первом запуске (сохранение геоданных в БД)
//                    point.setHero(savedHero);
//                    wayPointRepositories.save(point);
//                }
            } catch (IOException e) {
                log.error("Ошибка доступа к файлу: {}", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("I am starting...");
        runServer();
        log.info("{}", session);
        endpoint = new Endpoint();
        endpoint.setUserSession(session);
    }

    public static void runServer() {
        Server server = new Server("localhost", 8081, "/", Endpoint.class);
        log.info("Стартую сервер...");
        try {
            server.start();
            while (session == null){
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 1000)
    private void sendCoordinates() throws JsonProcessingException {
        if (endpoint != null) {
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
                    endpoint.onMessage(json);
                }
            }
            i++;
        } else {
            log.info("Пока ничего не делаю каждую секунду)))");
        }
    }

}
