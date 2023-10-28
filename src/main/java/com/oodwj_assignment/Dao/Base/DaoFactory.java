package com.oodwj_assignment.Dao.Base;

import com.oodwj_assignment.Dao.*;

/**
 * Factory method for singleton implementation of DAOs
 *
 */
public class DaoFactory {
    private static final AttachmentDao attachmentDao = new AttachmentDaoImpl();
    private static final CartDao cartDao = new CartDaoImpl();
    private static final FoodDao foodDao = new FoodDaoImpl();
    private static final NotificationDao notificationDao = new NotificationDaoImpl();
    private static final OrderDao orderDao = new OrderDaoImpl();
    private static final OrderFoodDao orderFoodDao = new OrderFoodDaoImpl();
    private static final ReceiptDao receiptDao = new ReceiptDaoImpl();
    private static final ReviewDao reviewDao = new ReviewDaoImpl();
    private static final SessionDao sessionDao = new SessionDaoImpl();
    private static final StoreDao storeDao = new StoreDaoImpl();
    private static final TaskDao taskDao = new TaskDaoImpl();
    private static final TransactionDao transactionDao = new TransactionDaoImpl();
    private static final UserDao userDao = new UserDaoImpl();
    private static final WalletDao walletDao = new WalletDaoImpl();

    public static AttachmentDao getAttachmentDao() {
        return attachmentDao;
    }

    public static CartDao getCartDao() {
        return cartDao;
    }

    public static FoodDao getFoodDao() {
        return foodDao;
    }

    public static NotificationDao getNotificationDao() {
        return notificationDao;
    }

    public static OrderDao getOrderDao() {
        return orderDao;
    }

    public static OrderFoodDao getOrderFoodDao() {
        return orderFoodDao;
    }

    public static ReceiptDao getReceiptDao() {
        return receiptDao;
    }

    public static ReviewDao getReviewDao() {
        return reviewDao;
    }

    public static SessionDao getAuthDao() {
        return sessionDao;
    }

    public static StoreDao getStoreDao() {
        return storeDao;
    }

    public static TaskDao getTaskDao() {
        return taskDao;
    }

    public static TransactionDao getTransactionDao() {
        return transactionDao;
    }

    public static UserDao getUserDao() {
        return userDao;
    }

    public static WalletDao getWalletDao() {
        return walletDao;
    }
}
