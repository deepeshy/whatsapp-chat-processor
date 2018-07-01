package com.dy.chat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Chat {

  private static class Message {

    private String message;
    private LocalDate date;

    public Message(String message, LocalDate date) {
      this.message = message;
      this.date = date;
    }

    public void append(String extraMsg) {
      this.message = this.message + "\n" + extraMsg;
    }

    public Message(String rawText) {
      String[] parts = rawText.split("]");
      String datePart = parts[0].replace("[", "");
      if (parts.length < 2) {
        System.out.println(rawText);
      }
      String messagePart = parts[1];

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm, M/d/yyyy");
      LocalDate dateVal = LocalDate.parse(datePart, dtf);

      this.date = dateVal;
      this.message = messagePart;
    }

    @Override
    public String toString() {
      return message;
    }

    public String getMessage() {
      return message;
    }

    public LocalDate getDate() {
      return date;
    }
  }

  public static void main(String[] args) {
    List<Message> messages = new ArrayList<>();

    try {
      List<String> strings = Files
          .readAllLines(Paths.get("C:\\Users\\admin\\Desktop\\rawChat.txt"));
      Message messageBuffer = null;
      for (String x : strings) {
        if (partOfOldMessage(x)) {
          messageBuffer.append(x);
        } else {
          messageBuffer = new Message(x);
          messages.add(messageBuffer);
        }
      }
      System.out.println(messages);
    } catch (IOException e) {
      e.printStackTrace();
    }
    Map<LocalDate, Set<Message>> sortedMessages = messages.stream()
        .collect(Collectors.groupingBy(Message::getDate, Collectors.toSet()));

    Map lhm = new LinkedHashMap(sortedMessages);
    System.out.println(lhm);

  }

  private static boolean partOfOldMessage(String x) {
    return !x.contains("[");
  }

  private String rawText;

  public Chat(String rawText) {
    this.rawText = rawText;
  }

  public String format() {
    return "";
  }

}
