package com.homeBanking.accountsservice.service;

import com.homeBanking.accountsservice.entities.*;
import com.homeBanking.accountsservice.exceptions.CheckTransactionDataException;
import com.homeBanking.accountsservice.exceptions.InsufficientFundsException;
import com.homeBanking.accountsservice.exceptions.ResourceNotFoundException;
import com.homeBanking.accountsservice.repository.AccountsRepository;
import com.homeBanking.accountsservice.repository.FeignCardRepository;
import com.homeBanking.accountsservice.repository.FeignTransactionRepository;
import com.homeBanking.accountsservice.repository.FeignUserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class AccountsService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private FeignUserRepository feignUserRepository;

    @Autowired
    private FeignTransactionRepository feignTransactionRepository;

    @Autowired
    private FeignCardRepository feignCardRepository;


    public void createAccount(Long userId) {
        Account account = new Account(userId);
        accountsRepository.save(account);
    }

    public List<AccountInformation> getAccountInformation(String kcId) throws ResourceNotFoundException {
        Long userId = feignUserRepository.getUserByKeycloakId(kcId);
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isPresent()) {
            Account accountFound = accountOptional.get();
            UserDTO feignUser = feignUserRepository.getUserById(kcId);
            List<AccountInformation> accounts = new ArrayList<>();
            AccountInformation accountInformation = new AccountInformation(accountFound.getBalance(),
                    feignUser.getCvu(),
                    feignUser.getAlias(),
                    feignUserRepository.getKcByUserId(accountFound.getUserId()),
                    String.valueOf(accountFound.getId()),
                    feignUser.getFirstName() + " " + feignUser.getLastName());
            accounts.add(0,accountInformation);
            return accounts;
        } else {
            throw new ResourceNotFoundException("Account not found");
        }
    }

    public Account getAccountByUserId(Long userId) throws ResourceNotFoundException {

        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isPresent()) {
            return accountOptional.get();
        } else {
            throw new ResourceNotFoundException("Account not found");
        }
    }

    public Account getAccountByUserCvu(String cvu) throws ResourceNotFoundException {

        Long userId = feignUserRepository.getUserIdByCvu(cvu);
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isPresent()) {
            return accountOptional.get();
        } else {
            throw new ResourceNotFoundException("Account not found");
        }
    }

    public List<AccountInformation> getAllAccountInformation() {

        List<Account> accounts = accountsRepository.findAll();

        List<UserDTO> users = feignUserRepository.getAllUser();

        // 3. Crear Map de usuarios
        Map<String, UserDTO> userMap = users.stream()
                .collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        // 4. Mapear a AccountInformation
        return accounts.stream()
                .map(account -> {
                    UserDTO user = userMap.get(feignUserRepository.getKcByUserId(account.getUserId()));
                    if (user != null) {
                        return new AccountInformation(
                                account.getBalance(),
                                user.getCvu(),
                                user.getAlias(),
                                feignUserRepository.getKcByUserId(account.getUserId()),
                                String.valueOf(account.getId()),
                                user.getFirstName() + " " + user.getLastName()
                        );
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Transaction> getLastFiveTransactions(Long userId) throws ResourceNotFoundException {
        String cvu = feignUserRepository.getCvuByUserId(userId);
        Account account = getAccountByUserId(userId);
        List<Transaction> transactions = feignTransactionRepository.getLastFiveTransactions(cvu,account.getId());
         if(transactions.isEmpty()){
             throw new ResourceNotFoundException("No transactions found");
         }
         return transactions;
    }

    public List<Transaction> getAllTransactions(Long userId) throws ResourceNotFoundException {
        String cvu = feignUserRepository.getCvuByUserId(userId);
        Account account = getAccountByUserId(userId);
        List<Transaction> transactions = feignTransactionRepository.getAllTransactions(cvu,account.getId());
        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("No transactions found");
        }
        return transactions;
    }


    public Transaction getTransaction(Long userId, Long activityId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            return feignTransactionRepository.getTransaction(account.getId(), activityId);
        }
    }

    public Card registerCard(CardRequest cardRequest, Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            LocalDate expirationDate = parseExpiration(cardRequest.getExpiration());
            var newCard = CardResquestWithAccountId.builder()
                    .accountId(account.getId())
                    .holder(cardRequest.getName())
                    .number(cardRequest.getNumber())
                    .expirationDate(expirationDate)
                    .cvv(cardRequest.getCvc())
                    .build();
            return feignCardRepository.registerCard(newCard);
        }
    }

    public List<Card> getAllCards(Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            return feignCardRepository.getAllCardsByAccountId(account.getId());
        }
    }

    public Card getCardById(Long cardId, Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            return feignCardRepository.getCardByIdAndAccountId(account.getId(), cardId);
        }
    }

    public Card getCardByNumber(String cardNumber, Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            return feignCardRepository.getCardByNumberAndAccountId(account.getId(), cardNumber);
        }
    }

    public void deleteCardByNumber(Long cardId, Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            feignCardRepository.deleteCard(account.getId(),cardId);
        }
    }

    public Long getUserIdByKcId(String kcId) {
        Long userId = feignUserRepository.getUserByKeycloakId(kcId);
        return userId;
    }

    @Transactional
    public void updateAmountAccount(TransactionRequest transactionRequest,Long userId) throws ResourceNotFoundException,InsufficientFundsException, IllegalArgumentException {

    switch (transactionRequest.getType()) {
        case "Deposit" -> {
            Account account = getAccountByUserId(userId);
            account.setBalance(account.getBalance() + transactionRequest.getAmount());
            accountsRepository.save(account);
        }
        case "Transfer" -> {
            Account origin = getAccountByUserCvu(transactionRequest.getOrigin());
            Account destination = getAccountByUserCvu(transactionRequest.getDestination());
            double amount = Math.abs(transactionRequest.getAmount());

            if (origin.getBalance() < amount)
                throw new InsufficientFundsException("Insufficient funds");

            origin.setBalance(origin.getBalance() - amount);
            destination.setBalance(destination.getBalance() + amount);
            accountsRepository.save(origin);
            accountsRepository.save(destination);
        }
        default -> throw new IllegalArgumentException("Unsupported transaction type: " + transactionRequest.getType());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long registerTransaction(TransactionRequest transactionRequest, Long userId) throws ResourceNotFoundException, InsufficientFundsException, CheckTransactionDataException {

        if(transactionRequest.getType().equals("Transfer")){
            checkTransactionData(transactionRequest);
        }

        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            updateAmountAccount(transactionRequest,userId);
            var newTransaction = TransactionRequestWithAccountId.builder()
                    .accountId(account.getId())
                    .type(transactionRequest.getType())
                    .amount(transactionRequest.getAmount())
                    .origin(transactionRequest.getOrigin())
                    .destination(transactionRequest.getDestination())
                    .name(transactionRequest.getName())
                    .description(transactionRequest.getDescription())
                    .build();
            return feignTransactionRepository.createTransaction(newTransaction);
        }
    }

    private void checkTransactionData(TransactionRequest transactionRequest) throws CheckTransactionDataException{

        if(transactionRequest.getDestination().isEmpty()) {
            throw new CheckTransactionDataException("No destiny cvu added");
        }
        if(transactionRequest.getAmount() == 0.0) {
            throw new CheckTransactionDataException("No amount added");
        }
    }

    public static LocalDate parseExpiration(String expiration) {
        if (expiration == null || expiration.length() != 4) {
            throw new IllegalArgumentException("Formato de expiración inválido. Se esperaba MMYY (por ejemplo, 0528)");
        }

        try {
            int month = Integer.parseInt(expiration.substring(0, 2));
            int year = Integer.parseInt(expiration.substring(2, 4)) + 2000; // asumimos siglo 21

            // Validar mes entre 1 y 12
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Mes inválido en la fecha de expiración: " + month);
            }

            // Devolver el último día del mes
            return YearMonth.of(year, month).atEndOfMonth();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de expiración inválido. Solo se permiten números (MMYY).", e);
        }
    }

}
