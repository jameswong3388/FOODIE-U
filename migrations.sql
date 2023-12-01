create table Users
(
    userId      varchar(255) not null
        primary key,
    username    varchar(255) not null,
    password    varchar(255) not null,
    role        varchar(255) not null,
    name        varchar(255) not null,
    phoneNumber varchar(255) not null,
    email       varchar(255) not null,
    updatedAt   datetime null,
    createdAt   datetime null
);

create table Notifications
(
    notificationId varchar(255) not null
        primary key,
    message        varchar(255) not null,
    status         varchar(255) not null,
    type           varchar(255) not null,
    userId         varchar(255) not null,
    updatedAt      datetime     not null,
    createdAt      datetime     not null,

    foreign key (userId) references Users (userId)
);

create table Attachments
(
    attachmentId   varchar(255) not null
        primary key,
    notificationId varchar(255) not null,
    attachedId     varchar(255) not null,
    updatedAt      datetime     not null,
    createdAt      datetime     not null,

    foreign key (notificationId) references Notifications (notificationId)
);


create table Orders
(
    orderId       varchar(255) not null
        primary key,
    userId        varchar(255) not null,
    totalPrice    int          not null,
    totalQuantity int          not null,
    status        varchar(255) not null,
    type          varchar(255) not null,
    transactionId varchar(255) not null,
    updatedAt     datetime     not null,
    createdAt     datetime     not null,

    foreign key (userId) references Users (userId),
    foreign key (transactionId) references Transactions (transactionId)
);

create table Receipts
(
    receiptId varchar(255) not null
        primary key,
    userId    varchar(255) not null,
    transactionId double not null,
    updatedAt datetime     not null,
    createdAt datetime     not null,

    foreign key (userId) references Users (userId),
    foreign key (transactionId) references Transactions (transactionId)
);

create table Reviews
(
    reviewId      varchar(255) not null
        primary key,
    model         varchar(255) not null,
    modelUUID     varchar(255) not null,
    userId        varchar(255) not null,
    reviewContent text         not null,
    reviewRating  varchar(255) not null,
    updatedAt     datetime     not null,
    createdAt     datetime     not null,

    foreign key (orderId) references Orders (orderId),
    foreign key (userId) references Users (userId)
);

create table Stores
(
    storeId     varchar(255) not null
        primary key,
    name        varchar(255) not null,
    vendorId    varchar(255) not null,
    description text null,
    category    varchar(255) not null,
    updatedAt   datetime     not null,
    createdAt   datetime     not null,

    foreign key (vendorId) references Users (userId)
);

create table Foods
(
    foodId    varchar(255) not null
        primary key,
    storeId   varchar(255) not null,
    foodName  varchar(255) not null,
    foodType  varchar(255) not null,
    foodPrice varchar(255) not null,
    updatedAt datetime     not null,
    createdAt datetime     not null,

    foreign key (storeId) references Stores (storeId)
);

create table OrderFoods
(
    orderFoodId  varchar(255) not null
        primary key,
    foodId       varchar(255) not null,
    orderId      varchar(255) not null,
    foodName     varchar(255) not null,
    foodPrice    varchar(255) not null,
    amount       int          not null,
    foodQuantity int          not null,
    updatedAt    datetime     not null,
    createdAt    datetime     not null,

    foreign key (foodId) references Foods (foodId),
    foreign key (orderId) references Orders (orderId)
);

create table Tasks
(
    taskId        varchar(255) not null
        primary key,
    runnerId      varchar(255) not null,
    orderId       varchar(255) not null,
    deliveryFee   int          not null,
    status        varchar(255) not null,
    transactionId varchar(255) not null,
    updatedAt     datetime     not null,
    createdAt     datetime     not null,

    foreign key (runnerId) references Users (userId),
    foreign key (orderId) references Orders (orderId),
    foreign key (transactionId) references Transactions (transactionId)
);

create table Transactions
(
    transactionId varchar(255) not null
        primary key,
    amount double not null,
    type          varchar(255) not null,
    status        varchar(255) not null,
    payerId       varchar(255) not null,
    payeeId       varchar(255) not null,
    updatedAt     datetime     not null,
    createdAt     datetime     not null,

    foreign key (payerId) references Users (userId),
    foreign key (payeeId) references Users (userId)
);

create table Wallets
(
    walletId  varchar(255) not null
        primary key,
    userId    varchar(255) not null,
    credit double null,
    updatedAt datetime null,
    createdAt datetime     not null,

    foreign key (userId) references Users (userId)
);

create table Carts
(
    cartId       varchar(255) not null
        primary key,
    userId       varchar(255) not null,
    foodId       varchar(255) not null,
    foodName     varchar(255) not null,
    foodPrice double not null,
    foodQuantity int          not null,
    updatedAt    datetime     not null,
    createdAt    datetime     not null,

    foreign key (userId) references Users (userId),
    foreign key (foodId) references Foods (foodId)
)

create table Sessions
(
    sessionId         varchar(255) not null
        primary key,
    userId            varchar(255) not null,
    startTime         datetime     not null,
    endTime           datetime     not null,
    duration          int          not null,
    ipAddress         varchar(255) not null,
    userAgent         varchar(255) not null,
    location          varchar(255) not null,
    deviceInfo        varchar(255) not null,
    isAuthenticated   boolean      not null,
    referer           varchar(255) not null,
    terminationReason varchar(255) not null,
    isActive          boolean      not null,
    sessionToken      varchar(255) not null,
    updatedAt         datetime     not null,
    createdAt         datetime     not null,

    foreign key (userId) references Users (userId)
);

create table Medias
(
    mediaId    varchar(255) not null
        primary key,
    model      varchar(255) not null,
    modelUUID  varchar(255) not null,
    collection varchar(255) not null,
    fileName   varchar(255) not null,
    mimeType   varchar(255) not null,
    disk       varchar(255) not null,
    height     int          not null,
    width      int          not null,
    size       int          not null,
    updatedAt  datetime     not null,
    createdAt  datetime     not null
);

create table AdminProfiles
(
    adminProfileId varchar(255) not null
        primary key,
    userId         varchar(255) not null,
    gender         varchar(255) not null,
    dob            datetime     not null,
    updatedAt      datetime     not null,
    createdAt      datetime     not null,

    foreign key (userId) references Users (userId)
);

create table VendorProfiles
(
    vendorProfileId varchar(255) not null
        primary key,
    userId          varchar(255) not null,
    updatedAt       datetime     not null,
    createdAt       datetime     not null,

    foreign key (userId) references Users (userId)
)

create table CustomerProfiles
(
    customerProfileId varchar(255) not null
        primary key,
    userId            varchar(255) not null,

    gender            varchar(255) not null,
    dob               datetime     not null,
    updatedAt         datetime     not null,
    createdAt         datetime     not null,

    foreign key (userId) references Users (userId)
)

create table RunnerProfiles
(
    runnerProfileId varchar(255) not null
        primary key,
    userId          varchar(255) not null,

    gender          varchar(255) not null,
    dob             datetime     not null,
    updatedAt       datetime     not null,
    createdAt       datetime     not null,

    foreign key (userId) references Users (userId)
)
