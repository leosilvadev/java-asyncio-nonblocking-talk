package com.github.leosilvadev.nonblockingjava;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by leonardo on 5/7/18.
 */
public class Class {

  public static void main(String[] args) throws Exception {
    List<User> users = new ArrayList<>();
    Files.readAllLines(Paths.get("/home/leonardo/Downloads/users.csv")).stream()
        .skip(1)
        .forEach(line -> {
          String[] fields = line.split(",");
          String name = fields[2].replaceAll("\"", "");
          String email = fields[3].replaceAll("\"", "");
          String course = fields[4].replaceAll("\"", "");
          String registered = fields[5].replaceAll("\"", "");
          User user = new User(name, email, registered, course);
          int index = users.indexOf(user);
          if (index >= 0) {
            users.get(index).addCourse(course);
          } else {
            users.add(user);
          }
        });

    /*System.out.println("_____________________________________________________________________");
    System.out.println("Alunos cadastrados em um curso:");
    users.stream().filter(user -> user.getCourses().size() == 1)
        .sorted(Comparator.comparing(User::getName))
        .forEach(System.out::println);
    System.out.println("_____________________________________________________________________\n\n\n");

    System.out.println("_____________________________________________________________________");
    System.out.println("Alunos cadastrados em mais de um curso:");
    users.stream().filter(user -> user.getCourses().size() > 1)
        .sorted(Comparator.comparing(User::getName))
        .forEach(System.out::println);
    System.out.println("_____________________________________________________________________");*/

    System.out.println("_____________________________________________________________________");
    System.out.println("Alunos cadastrados por data:");
    Map<LocalDate, List<User>> groupedUsers = users.stream()
        .sorted(Comparator.comparing(User::getRegistered))
        .collect(Collectors.groupingBy(user -> user.getRegistered().toLocalDate()));

    List<LocalDate> sortedRegistrations = groupedUsers.keySet().stream().sorted().collect(Collectors.toList());

    for(LocalDate date : sortedRegistrations) {
      List<User> usersFound = groupedUsers.get(date);
      System.out.println("\n("+usersFound.size()+") Registrados em "+date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
      usersFound.forEach(System.out::println);
    }
    System.out.println("_____________________________________________________________________");


  }

  public static class User {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private String name;
    private String email;
    private List<String> courses;
    private LocalDateTime registered;

    public User(final String name, final String email, final String registered, final String course) {
      this.name = name;
      this.email = email;
      this.registered = LocalDateTime.parse(registered, formatter);
      this.courses = new ArrayList<>();
      addCourse(course);
    }


    public String getName() {
      return name;
    }

    public List<String> getCourses() {
      return courses;
    }

    public void addCourse(String course) {
      getCourses().add(course);
    }

    public LocalDateTime getRegistered() {
      return registered;
    }

    public String getEmail() {
      return email;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final User user = (User) o;

      return email != null ? email.equals(user.email) : user.email == null;
    }

    @Override
    public int hashCode() {
      return email != null ? email.hashCode() : 0;
    }

    @Override
    public String toString() {
      return "User{" +
          "name='" + name + '\'' +
          ", email='" + email + '\'' +
          ", registered=" + registered +
          ", courses=" + courses +
          '}';
    }
  }

}
