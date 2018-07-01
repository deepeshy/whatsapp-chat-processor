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

  List<Message> messages;

  public Chat(String filename) {
    this.messages = new ArrayList<>();

    try {
      List<String> strings = Files
          .readAllLines(Paths.get(filename));
      Message messageBuffer = null;
      long messageIdLocal = 1;
      for (String x : strings) {
        x = x.replace("Deepesh Yadav:", "");
        if (partOfOldMessage(x)) {
          messageBuffer.append(x);
        } else {
          messageBuffer = new Message(x, messageIdLocal);
          messageIdLocal++;
          this.messages.add(messageBuffer);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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
//      return message;
      return "(" + timestamp.getHour() + ":" + timestamp.getMinute() + ") " + message;
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
    // TODO validation checks for filename
    System.out.println(new Chat(args[0]).format());
  }

  private static boolean partOfOldMessage(String x) {
    return !x.contains("[");
  }

  public String format() {

    StringBuilder sb = new StringBuilder();

    Map<LocalDate, Set<Message>> sortedMessages = messages.stream()
        .collect(Collectors.groupingBy(Message::getDate, Collectors.toSet()));

    Map<LocalDate, Set<Message>> lhm = new TreeMap(sortedMessages);
    for (Map.Entry<LocalDate, Set<Message>> ent : lhm.entrySet()) {
      sb.append("\n\n********************************************************************\n");
      sb.append("\t" + ent.getKey());
      sb.append("\n");
      sb.append("********************************************************************\n");
      Set<Message> internallySorted = new TreeSet<>(ent.getValue());
      internallySorted.stream().forEach(x -> sb.append(x.toString() + "\n"));
    }
    return sb.toString();
  }

}
