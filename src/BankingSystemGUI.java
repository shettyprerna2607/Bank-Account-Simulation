import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Base Account class
abstract class Account {
    protected double balance = 0.0;

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    // Withdraw money if amount > 0 and minimum balance maintained
    public void withdrawal(double amount) {
        if (amount > 0 && balance - amount >= getMinimumBalance()) {
            balance -= amount;
        }
    }

    // Minimum balance each account type should maintain
    protected abstract double getMinimumBalance();
}

// Savings Account with interest
class SavingsAccount extends Account {
    private static final double MIN_BALANCE = 50.0;
    private static final double INTEREST_RATE = 0.05; // 5%

    @Override
    protected double getMinimumBalance() {
        return MIN_BALANCE;
    }

    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
    }

    @Override
    public void withdrawal(double amount) {
        if (amount > 0) {
            if (amount <= balance - MIN_BALANCE) {
                balance -= amount;
            }
        }
    }
}

// Current Account with higher minimum balance
class CurrentAccount extends Account {
    private static final double MIN_BALANCE = 200.0;

    @Override
    protected double getMinimumBalance() {
        return MIN_BALANCE;
    }

    @Override
    public void withdrawal(double amount) {
        if (amount > 0) {
            if (amount <= balance - MIN_BALANCE) {
                balance -= amount;
            }
        }
    }
}

// Bank class with deposit method
class Bank {
    public void deposit(double amount, Account account) {
        account.deposit(amount);
    }
}

// GUI Class
public class BankingSystemGUI extends JFrame {
    Bank bank = new Bank();
    SavingsAccount savingsAccount = new SavingsAccount();
    CurrentAccount currentAccount = new CurrentAccount();

    private JTabbedPane tabbedPane;

    private JTextField savDepositField, savWithdrawField;
    private JLabel savBalanceLabel;

    private JTextField currDepositField, currWithdrawField;
    private JLabel currBalanceLabel;

    public BankingSystemGUI() {
        setTitle("Banking System");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // Savings Account Panel Layout Setup
        JPanel savingsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Deposit label, input, and button
        gbc.gridx = 0; gbc.gridy = 0;
        savingsPanel.add(new JLabel("Deposit Amount:"), gbc);

        gbc.gridx = 1;
        savDepositField = new JTextField(15);
        savingsPanel.add(savDepositField, gbc);

        gbc.gridx = 2;
        JButton savDepositBtn = new JButton("Deposit");
        savingsPanel.add(savDepositBtn, gbc);

        // Row 1: Withdrawal label, input, and button
        gbc.gridx = 0; gbc.gridy = 1;
        savingsPanel.add(new JLabel("Withdraw Amount:"), gbc);

        gbc.gridx = 1;
        savWithdrawField = new JTextField(15);
        savingsPanel.add(savWithdrawField, gbc);

        gbc.gridx = 2;
        JButton savWithdrawBtn = new JButton("Withdraw");
        savingsPanel.add(savWithdrawBtn, gbc);

        // Row 2: Interest and Check Balance buttons
        gbc.gridx = 0; gbc.gridy = 2;
        JButton savInterestBtn = new JButton("Apply Interest");
        savingsPanel.add(savInterestBtn, gbc);

        gbc.gridx = 1;
        JButton savCheckBalanceBtn = new JButton("Check Balance");
        savingsPanel.add(savCheckBalanceBtn, gbc);

        // Row 3: Balance display label spanning all columns
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 3;
        savBalanceLabel = new JLabel("Balance: 0.00");
        savBalanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        savBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        savingsPanel.add(savBalanceLabel, gbc);

        // Current Account Panel Layout Setup
        JPanel currentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cGbc = new GridBagConstraints();
        cGbc.insets = new Insets(8, 8, 8, 8);
        cGbc.fill = GridBagConstraints.HORIZONTAL;

        cGbc.gridx = 0; cGbc.gridy = 0;
        currentPanel.add(new JLabel("Deposit Amount:"), cGbc);

        cGbc.gridx = 1;
        currDepositField = new JTextField(15);
        currentPanel.add(currDepositField, cGbc);

        cGbc.gridx = 2;
        JButton currDepositBtn = new JButton("Deposit");
        currentPanel.add(currDepositBtn, cGbc);

        cGbc.gridx = 0; cGbc.gridy = 1;
        currentPanel.add(new JLabel("Withdraw Amount:"), cGbc);

        cGbc.gridx = 1;
        currWithdrawField = new JTextField(15);
        currentPanel.add(currWithdrawField, cGbc);

        cGbc.gridx = 2;
        JButton currWithdrawBtn = new JButton("Withdraw");
        currentPanel.add(currWithdrawBtn, cGbc);

        cGbc.gridx = 0; cGbc.gridy = 2;
        JButton currCheckBalanceBtn = new JButton("Check Balance");
        currentPanel.add(currCheckBalanceBtn, cGbc);

        cGbc.gridx = 0; cGbc.gridy = 3;
        cGbc.gridwidth = 3;
        currBalanceLabel = new JLabel("Balance: 0.00");
        currBalanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentPanel.add(currBalanceLabel, cGbc);

        tabbedPane.addTab("Savings Account", savingsPanel);
        tabbedPane.addTab("Current Account", currentPanel);

        add(tabbedPane);

        // Action listeners for Savings Account buttons
        savDepositBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(savDepositField.getText());
                if (amount <= 0) throw new NumberFormatException();
                bank.deposit(amount, savingsAccount);
                updateSavingsBalance();
                JOptionPane.showMessageDialog(this, "Deposit successful!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid positive number for deposit.");
            }
        });

        savWithdrawBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(savWithdrawField.getText());
                if (amount <= 0) throw new NumberFormatException();

                double oldBalance = savingsAccount.getBalance();
                savingsAccount.withdrawal(amount);

                if (savingsAccount.getBalance() != oldBalance) {
                    JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Withdrawal failed! Minimum balance not maintained.");
                }
                updateSavingsBalance();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid positive number for withdrawal.");
            }
        });

        savInterestBtn.addActionListener(e -> {
            savingsAccount.calculateInterest();
            updateSavingsBalance();
            JOptionPane.showMessageDialog(this, "Interest applied.");
        });

        savCheckBalanceBtn.addActionListener(e -> {
            updateSavingsBalance();
            JOptionPane.showMessageDialog(this, "Current balance: " + String.format("%.2f", savingsAccount.getBalance()));
        });

        // Action listeners for Current Account buttons
        currDepositBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(currDepositField.getText());
                if (amount <= 0) throw new NumberFormatException();
                bank.deposit(amount, currentAccount);
                updateCurrentBalance();
                JOptionPane.showMessageDialog(this, "Deposit successful!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid positive number for deposit.");
            }
        });

        currWithdrawBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(currWithdrawField.getText());
                if (amount <= 0) throw new NumberFormatException();

                double oldBalance = currentAccount.getBalance();
                currentAccount.withdrawal(amount);

                if (currentAccount.getBalance() != oldBalance) {
                    JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Withdrawal failed! Minimum balance not maintained.");
                }
                updateCurrentBalance();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid positive number for withdrawal.");
            }
        });

        currCheckBalanceBtn.addActionListener(e -> {
            updateCurrentBalance();
            JOptionPane.showMessageDialog(this, "Current balance: " + String.format("%.2f", currentAccount.getBalance()));
        });
    }

    // Update Savings Account balance display
    private void updateSavingsBalance() {
        savBalanceLabel.setText("Balance: " + String.format("%.2f", savingsAccount.getBalance()));
    }

    // Update Current Account balance display
    private void updateCurrentBalance() {
        currBalanceLabel.setText("Balance: " + String.format("%.2f", currentAccount.getBalance()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BankingSystemGUI().setVisible(true);
        });
    }
}
