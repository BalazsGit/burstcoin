package brs.services.impl;

import brs.Account;
import brs.Subscription;
import brs.db.BurstIterator;
import brs.db.BurstKey;
import brs.db.BurstKey.LongKeyFactory;
import brs.db.VersionedEntityTable;
import brs.db.store.SubscriptionStore;
import brs.services.SubscriptionService;

public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionStore subscriptionStore;
  private final VersionedEntityTable<Subscription> subscriptionTable;
  private final LongKeyFactory<Subscription> subscriptionDbKeyFactory;

  public SubscriptionServiceImpl(SubscriptionStore subscriptionStore) {
    this.subscriptionStore = subscriptionStore;
    this.subscriptionTable = subscriptionStore.getSubscriptionTable();
    this.subscriptionDbKeyFactory = subscriptionStore.getSubscriptionDbKeyFactory();
  }

  @Override
  public Subscription getSubscription(Long id) {
    return subscriptionTable.get(subscriptionDbKeyFactory.newKey(id));
  }

  @Override
  public BurstIterator<Subscription> getSubscriptionsByParticipant(Long accountId) {
    return subscriptionStore.getSubscriptionsByParticipant(accountId);
  }

  @Override
  public BurstIterator<Subscription> getSubscriptionsToId(Long accountId) {
    return subscriptionStore.getSubscriptionsToId(accountId);
  }

  @Override
  public void addSubscription(Account sender, Account recipient, Long id, Long amountNQT, int startTimestamp, int frequency) {
    final BurstKey dbKey = subscriptionDbKeyFactory.newKey(id);
    final Subscription subscription = new Subscription(sender.getId(), recipient.getId(), id, amountNQT, frequency, startTimestamp, dbKey);

    subscriptionTable.insert(subscription);
  }

}
