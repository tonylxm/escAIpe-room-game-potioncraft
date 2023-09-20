package nz.ac.auckland.se206.gpt;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class ChatHandler {
  private ChatCompletionRequest chatCompletionRequest;
  private Choice result;
  private Task<Void> appendTask;

  @FXML
  public void initialize() throws ApiProxyException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2)
          .setTopP(0.5).setMaxTokens(100);
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public String runGpt(String msgContent) throws ApiProxyException {
    ChatMessage msg = new ChatMessage("user", msgContent);
    chatCompletionRequest.addMessage(msg);
    try {
      // Sending a request to the GPT API proxy to get a response for the user
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage().getContent();
    } catch (ApiProxyException e) {
      // Displaying stack trace and not crashing the game if there is a request error
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  public void appendChatMessage(
    ChatMessage msg, TextArea chatTextArea, TextField inputText, Button sendButton) {
    // Adding the role of the chatter to the start of each message
    String displayRole;
    switch (msg.getRole()) {
      case "assistant":
        displayRole = "Wizard";
        break;
      case "user":
        displayRole = "You";
        break;
      default:
        displayRole = msg.getRole();
        break;
    }

    chatTextArea.appendText(displayRole + ": ");

    // Appending the message character by character to the chat text area
    appendTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            for (char c : msg.getContent().toCharArray()) {
              chatTextArea.appendText(String.valueOf(c));
              Thread.sleep(20);
            }
            chatTextArea.appendText("\n\n");
            return null;
          }
        };
    new Thread(appendTask, "Append Thread").start();

    // Not allowing the user to send messages while the wizard or assistant is typing
    if (displayRole.equals("Wizard")) {
      appendTask.setOnSucceeded(
          e -> {
            inputText.setDisable(false);
            sendButton.setDisable(false);
            if (inputText.getOpacity() == 0.5) {
              inputText.setOpacity(1);
              sendButton.setOpacity(1);
            }
          });
    }
  }

  public void setResult(Choice result) {
    this.result = result;
  }

  public Choice getResult() {
    return result;
  }

  public Task<Void> getAppendTask() {
    return appendTask;
  }

  public void setAppendTask(Task<Void> appendTask) {
    this.appendTask = appendTask;
  }
}
