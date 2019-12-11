/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mar;

import static java.lang.Thread.sleep;

/**
 *
 * @author Anthony Caliri
 */
class MessageHandler {
    //Do with and without wait

    String message;

    synchronized public void send(String msg) {

        if (message != null) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        System.out.println("Sending parent msg...");
        message = msg;
    }

    synchronized public void receive() {
        if (message != null) {
            System.out.println(message);
            message = null;
            notify();
        }
    }
}

class WaitingMessageThreadSend extends Thread {

    private MessageHandler sender;
    private String message;

    public WaitingMessageThreadSend(MessageHandler aSender, String aMessage) {
        sender = aSender;
        message = aMessage;
    }

    @Override
    public void run() {
        try {
            sender.send(message);
            sleep(1000);
        } catch (InterruptedException e) {
        }
    }
}

class WaitingMessageThreadReceive extends Thread {

    private MessageHandler sender;

    public WaitingMessageThreadReceive(MessageHandler aSender) {
        sender = aSender;
    }

    @Override
    public void run() {
        sender.receive();
    }
}
