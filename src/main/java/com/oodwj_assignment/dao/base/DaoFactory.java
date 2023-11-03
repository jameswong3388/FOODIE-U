package com.oodwj_assignment.dao.base;

import com.oodwj_assignment.dao.*;
import com.oodwj_assignment.dao.AttachmentDaoImpl;
import com.oodwj_assignment.dao.users.UserDao;
import com.oodwj_assignment.dao.users.UserDaoImpl;

/**
 * Factory method for singleton implementation of DAOs
 */
public class DaoFactory {
    private static final AttachmentDao attachmentDao = new AttachmentDaoImpl();
    private static final CartDao cartDao = new CartDaoImpl();
    private static final FoodDao foodDao = new FoodDaoImpl();
    private static final MediaDao mediaDao = new MediaDaoImpl();
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

    private DaoFactory() {}

    /**
     * Attachment dao attachment dao.
     *
     * @return the attachment dao
     */
    public static AttachmentDao getAttachmentDao() {
        return attachmentDao;
    }

    /**
     * Cart dao cart dao.
     *
     * @return the cart dao
     */
    public static CartDao getCartDao() {
        return cartDao;
    }

    /**
     * Food dao food dao.
     *
     * @return the food dao
     */
    public static FoodDao getFoodDao() {
        return foodDao;
    }

    /**
     * Media dao media dao.
     *
     * @return the media dao
     */
    public static MediaDao getMediaDao() {
        return mediaDao;
    }

    /**
     * Notification dao notification dao.
     *
     * @return the notification dao
     */
    public static NotificationDao getNotificationDao() {
        return notificationDao;
    }

    /**
     * Order dao order dao.
     *
     * @return the order dao
     */
    public static OrderDao getOrderDao() {
        return orderDao;
    }

    /**
     * Order food dao order food dao.
     *
     * @return the order food dao
     */
    public static OrderFoodDao getOrderFoodDao() {
        return orderFoodDao;
    }

    /**
     * Receipt dao receipt dao.
     *
     * @return the receipt dao
     */
    public static ReceiptDao getReceiptDao() {
        return receiptDao;
    }

    /**
     * Review dao review dao.
     *
     * @return the review dao
     */
    public static ReviewDao getReviewDao() {
        return reviewDao;
    }

    /**
     * Session dao session dao.
     *
     * @return the session dao
     */
    public static SessionDao getAuthDao() {
        return sessionDao;
    }

    /**
     * Store dao store dao.
     *
     * @return the store dao
     */
    public static StoreDao getStoreDao() {
        return storeDao;
    }

    /**
     * Task dao task dao.
     *
     * @return the task dao
     */
    public static TaskDao getTaskDao() {
        return taskDao;
    }

    /**
     * Transaction dao transaction dao.
     *
     * @return the transaction dao
     */
    public static TransactionDao getTransactionDao() {
        return transactionDao;
    }

    /**
     * User dao user dao.
     *
     * @return the user dao
     */
    public static UserDao getUserDao() {
        return userDao;
    }

    /**
     * Wallet dao wallet dao.
     *
     * @return the wallet dao
     */
    public static WalletDao getWalletDao() {
        return walletDao;
    }
}
