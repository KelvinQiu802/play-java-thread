import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ThreadTest {

    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.addUser(new User(0, 1000));
        bank.addUser(new User(1, 1000));
        bank.addUser(new User(2, 1000));
        bank.addUser(new User(3, 1000));

        Runnable task1 = () -> {
            for (int i = 0; i < 100; ++i) {
                bank.transfer(0, 1, 1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable task2 = () -> {
            for (int i = 0; i < 100; ++i) {
                bank.transfer(2, 3, 1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();
    }

}

class Bank {

    private final Map<Integer, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void transfer(int senderId, int receiverId, double amount) {
        User fromUser = users.get(senderId);
        User toUser = users.get(receiverId);
        fromUser.setBalance(fromUser.getBalance() - amount);
        toUser.setBalance(toUser.getBalance() + amount);
        Logger.getGlobal().info(String.format("Thread[%s] / User[%d] -> User[%d]: %.1f USD",
                Thread.currentThread().getName(),
                senderId, receiverId,
                amount));
    }

}

class User {

    private final int id;

    private double balance;

    public User(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public int getId() {
        return id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}