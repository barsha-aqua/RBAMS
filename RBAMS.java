import java.util.*;

// Custom Exception
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

// Abstract Class
abstract class BankAccount {
    private String accountHolderName;
    private String accountNumber;
    protected double balance;

    public BankAccount(String accountHolderName, String accountNumber, double balance) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public abstract void withdraw(double amount) throws InsufficientBalanceException;
    public abstract void displayAccountDetails();

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful. New balance: Rs. " + balance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }
}

// SavingsAccount
class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount(String accountHolderName, String accountNumber, double balance, double interestRate) {
        super(accountHolderName, accountNumber, balance);
        this.interestRate = interestRate;
    }

    public void addInterest() {
        double interest = balance * interestRate / 100;
        balance += interest;
        System.out.println("Interest added: Rs. " + interest);
    }

    @Override
    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException("Withdrawal failed: Insufficient balance!");
        }
        balance -= amount;
        System.out.println("Withdrawal successful. New balance: Rs. " + balance);
    }

    @Override
    public void displayAccountDetails() {
        System.out.println("Savings Account - " + getAccountNumber() + " | Balance: Rs. " + balance);
    }
}

// CurrentAccount
class CurrentAccount extends BankAccount {
    private double overdraftLimit;

    public CurrentAccount(String accountHolderName, String accountNumber, double balance, double overdraftLimit) {
        super(accountHolderName, accountNumber, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount > (balance + overdraftLimit)) {
            throw new InsufficientBalanceException("Withdrawal failed: Exceeds overdraft limit!");
        }
        balance -= amount;
        System.out.println("Withdrawal successful. New balance: Rs. " + balance);
    }

    @Override
    public void displayAccountDetails() {
        System.out.println("Current Account - " + getAccountNumber() + " | Balance: Rs. " + balance);
    }
}

// Customer
class Customer {
    private String name;
    private List<BankAccount> accounts = new ArrayList<>();

    public Customer(String name) {
        this.name = name;
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public String getName() {
        return name;
    }
}

// Main class
public class RBAMS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Rural Bank of Nepal");

        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine();
        Customer customer = new Customer(customerName);

        // Create accounts
        SavingsAccount savings = new SavingsAccount(customerName, "SAV123", 5000, 5);
        CurrentAccount current = new CurrentAccount(customerName, "CUR456", 2000, 3000);

        customer.addAccount(savings);
        customer.addAccount(current);

        while (true) {
            System.out.println("\nChoose operation:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Add Interest to Savings");
            System.out.println("4. View Accounts");
            System.out.println("5. Exit");

            int choice = sc.nextInt();
            if (choice == 5) break;

            switch (choice) {
                case 1:
                    System.out.println("Select account (1: Savings, 2: Current): ");
                    int depAcc = sc.nextInt();
                    System.out.print("Enter amount: ");
                    double depAmt = sc.nextDouble();
                    if (depAcc == 1) savings.deposit(depAmt);
                    else if (depAcc == 2) current.deposit(depAmt);
                    else System.out.println("Invalid account.");
                    break;

                case 2:
                    System.out.println("Select account (1: Savings, 2: Current): ");
                    int withAcc = sc.nextInt();
                    System.out.print("Enter amount: ");
                    double withAmt = sc.nextDouble();
                    try {
                        if (withAcc == 1) savings.withdraw(withAmt);
                        else if (withAcc == 2) current.withdraw(withAmt);
                        else System.out.println("Invalid account.");
                    } catch (InsufficientBalanceException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    savings.addInterest();
                    break;

                case 4:
                    System.out.println("Accounts for " + customer.getName() + ":");
                    for (BankAccount acc : customer.getAccounts()) {
                        acc.displayAccountDetails();
                    }
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        System.out.println("Thank you for banking with us!");
        sc.close();
    }
}
