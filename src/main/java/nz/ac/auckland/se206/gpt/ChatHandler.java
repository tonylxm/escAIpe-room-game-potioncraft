package nz.ac.auckland.se206.gpt;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class ChatHandler {
  private ChatCompletionRequest chatCompletionRequest;
  public Choice result;

  @FXML
  public void initialize() throws ApiProxyException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
  }

  public String runGpt(String msgContent) throws ApiProxyException {
    ChatMessage msg = new ChatMessage("user", msgContent);
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage().getContent();
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
      return null;
    }
  }


  // TODO: Duplicate code, refactor later

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public ChatMessage runGptGameMaster(ChatMessage msg, TextArea chatTextArea) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      appendChatMessage(result.getChatMessage(), chatTextArea);
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  public void appendChatMessage(ChatMessage msg, TextArea chatTextArea) {
    chatTextArea.appendText(msg.getRole() + ": ");

    Task<Void> appendTask =
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
  }
}
