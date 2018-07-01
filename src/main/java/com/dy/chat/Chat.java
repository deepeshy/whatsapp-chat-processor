package com.dy.chat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Chat {

  private static class Message implements Comparable<Message> {

    private String message;
    private LocalDateTime timestamp;
    private long messageId;

    public void append(String extraMsg) {
      this.message = this.message + "\n" + extraMsg;
    }

    public Message(String rawText, long id) {
      String[] parts = rawText.split("]");
      String datePart = parts[0].replace("[", "");
      if (parts.length < 2) {
        System.out.println(rawText);
      }
      String messagePart = parts[1];

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm, M/d/yyyy");
      LocalDateTime timestampVal = LocalDateTime.parse(datePart, dtf);

      this.timestamp = timestampVal;
      this.message = messagePart;
      this.messageId = id;
    }

    @Override
    public String toString() {
//      return "(" + messageId+") " + message;
      return message;
    }

    public String getMessage() {
      return message;
    }

    public LocalDateTime getTimestamp() {
      return timestamp;
    }

    public LocalDate getDate() {
      return timestamp.toLocalDate();
    }

    @Override
    public int compareTo(Message o) {
      return Long.compare(messageId, o.messageId);
    }
  }

  public static void main(String[] args) {
    List<Message> messages = new ArrayList<>();

    try {
      List<String> strings = Files
          .readAllLines(Paths.get("C:\\Users\\admin\\Desktop\\rawChat.txt"));
      Message messageBuffer = null;
      long messageIdLocal = 1;
      for (String x : strings) {
        x = x.replace("Deepesh Yadav:", "");
        if (partOfOldMessage(x)) {
          messageBuffer.append(x);
        } else {
          messageBuffer = new Message(x, messageIdLocal);
          messageIdLocal++;
          messages.add(messageBuffer);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    Map<LocalDate, Set<Message>> sortedMessages = messages.stream()
        .collect(Collectors.groupingBy(Message::getDate, Collectors.toSet()));

    Map<LocalDate, Set<Message>> lhm = new TreeMap(sortedMessages);
    for (Map.Entry<LocalDate, Set<Message>> ent : lhm.entrySet()) {
      System.out
          .println("\n\n********************************************************************");
      System.out.println(ent.getKey());
      System.out.println("********************************************************************\n");
      Set<Message> internallySorted = new TreeSet<>(ent.getValue());
      internallySorted.stream().forEach(System.out::println);
    }
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
