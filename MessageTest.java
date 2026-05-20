package chatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @BeforeEach
    public void setUp() {
        chatapp.Message.resetState();
    }

    @Test
    public void testMessageLengthSuccess() {
        chatapp.Message msg = new chatapp.Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "A".repeat(260);
        chatapp.Message msg = new chatapp.Message("+27718693002", longMessage);
        assertEquals(
                "Message exceeds 250 characters by 10; please reduce the size.",
                msg.checkMessageLength()
        );
    }

    @Test
    public void testRecipientCellSuccess() {
        chatapp.Message msg = new chatapp.Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testRecipientCellFailure() {
        chatapp.Message msg = new chatapp.Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain " +
                        "an international code. Please correct the number and try again.",
                msg.checkRecipientCell()
        );
    }

    @Test
    public void testMessageHashFormat() {
        chatapp.Message msg = new chatapp.Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String hash = msg.getMessageHash();

        // Must be all uppercase
        assertEquals(hash, hash.toUpperCase());

        // Must have exactly 3 colon-separated parts
        String[] parts = hash.split(":");
        assertEquals(3, parts.length);

        // ID prefix must be 2 characters
        assertEquals(2, parts[0].length());

        // Message number part must be "0" (first message, counter starts at 0)
        assertEquals("0", parts[1]);

        assertEquals("HITONIGHT", parts[2]);
    }

    @Test
    public void testMessageHashBodyContent() {
        chatapp.Message msg = new chatapp.Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(msg.getMessageHash().endsWith(":HITONIGHT"));
    }

    @Test
    public void testMessageIDCreated() {
        chatapp.Message msg = new chatapp.Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(msg.checkMessageID());
        System.out.println("Message ID generated: " + msg.getMessageID());
    }

    @Test
    public void testMessageIDLength() {
        chatapp.Message msg = new chatapp.Message("+27718693002", "Test message");
        assertEquals(10, msg.getMessageID().length());
    }

    @Test
    public void testSentMessageSend() {
        chatapp.Message msg = new chatapp.Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", msg.sentMessage(1));
    }

    @Test
    public void testSentMessageDisregard() {
        chatapp.Message msg = new chatapp.Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", msg.sentMessage(2));
    }

    @Test
    public void testSentMessageStore() {
        chatapp.Message msg = new chatapp.Message("+27718693002", "Test store message");
        assertEquals("Message successfully stored.", msg.sentMessage(3));
    }

    @Test
    public void testReturnTotalMessages() {
        chatapp.Message msg1 = new chatapp.Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        msg1.sentMessage(1); // Send — counts

        chatapp.Message msg2 = new chatapp.Message("+27718693002", "Another message");
        msg2.sentMessage(2); // Disregard — does NOT count

        assertEquals(1, chatapp.Message.returnTotalMessages());
    }

    @Test
    public void testMessageHashesInLoop() {
        String[] recipients     = { "+27718693002", "+27718693002" };
        String[] messages       = {
                "Hi Mike, can you join us for dinner tonight?",
                "Hi there, hope you are well"
        };
        String[] expectedBodies = { "HITONIGHT", "HIWELL" };

        for (int i = 0; i < messages.length; i++) {
            chatapp.Message msg = new chatapp.Message(recipients[i], messages[i]);
            String[] parts = msg.getMessageHash().split(":");
            assertEquals(expectedBodies[i], parts[2],
                    "Hash body mismatch for message " + (i + 1));
        }
    }
}