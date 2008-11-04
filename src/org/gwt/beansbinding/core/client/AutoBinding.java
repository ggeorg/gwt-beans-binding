package org.gwt.beansbinding.core.client;

public class AutoBinding<SS, SV, TS, TV> extends Binding<SS, SV, TS, TV> {

  public enum UpdateStrategy {
    READ_ONCE, READ, READ_WRITE
  }

  private UpdateStrategy strategy;

  protected AutoBinding(UpdateStrategy strategy, SS sourceObject,
      Property<SS, SV> sourceProperty, TS targetObject,
      Property<TS, TV> targetProperty, String name) {
    super(sourceObject, sourceProperty, targetObject, targetProperty, name);

    if (strategy == null) {
      throw new IllegalArgumentException("must provide update strategy");
    }

    this.strategy = strategy;
  }

  /**
   * Returns the {@code AutoBinding's} update strategy.
   * 
   * @return the update strategy
   */
  public final UpdateStrategy getUpdateStrategy() {
    return strategy;
  }

  private final void tryRefreshThenSave() {
    SyncFailure refreshFailure = refresh();
    if (refreshFailure == null) {
      notifySynced();
    } else {
      SyncFailure saveFailure = save();
      if (saveFailure == null) {
        notifySynced();
      } else {
        notifySyncFailed(refreshFailure);
      }
    }
  }

  private final void trySaveThenRefresh() {
    SyncFailure saveFailure = save();
    if (saveFailure == null) {
      notifySynced();
    } else if (saveFailure.getType() == SyncFailureType.CONVERSION_FAILED
        || saveFailure.getType() == SyncFailureType.VALIDATION_FAILED) {
      notifySyncFailed(saveFailure);
    } else {
      SyncFailure refreshFailure = refresh();
      if (refreshFailure == null) {
        notifySynced();
      } else {
        notifySyncFailed(saveFailure);
      }
    }
  }

  @Override
  protected void bindImpl() {
    UpdateStrategy strat = getUpdateStrategy();

    if (strat == UpdateStrategy.READ_ONCE) {
      refreshAndNotify();
    } else if (strat == UpdateStrategy.READ) {
      refreshAndNotify();
    } else {
      tryRefreshThenSave();
    }
  }

  @Override
  protected void unbindImpl() {
    // Nothing to do here!
  }

  /**
   * Returns a string representing the internal state of the {@code Binding}.
   * This method is intended to be used for debugging purposes only, and the
   * content and format of the returned string may vary between implementations.
   * The returned string may be empty but may not be {@code null}.
   * 
   * @return a string representing the state of the {@code Binding}.
   */
  @Override
  protected String paramString() {
    return super.paramString() + ", updateStrategy=" + getUpdateStrategy();
  }

  @Override
  protected void sourceChangedImpl(PropertyStateEvent pse) {
    if (strategy == UpdateStrategy.READ_ONCE) {
      // nothing to do
    } else if (strategy == UpdateStrategy.READ) {
      if (pse.getValueChanged()) {
        refreshAndNotify();
      }
    } else if (strategy == UpdateStrategy.READ_WRITE) {
      if (pse.getValueChanged()) {
        tryRefreshThenSave();
      } else if (pse.isWriteable()) {
        saveAndNotify();
      }
    }
  }

  @Override
  protected void targetChangedImpl(PropertyStateEvent pse) {
    if (strategy == UpdateStrategy.READ_ONCE) {
      // nothing to do
    } else if (strategy == UpdateStrategy.READ) {
      if (pse.getWriteableChanged() && pse.isWriteable()) {
        refreshAndNotify();
      }
    } else if (strategy == UpdateStrategy.READ_WRITE) {
      if (pse.getWriteableChanged() && pse.isWriteable()) {
        if (pse.getValueChanged()) {
          tryRefreshThenSave();
        } else {
          refreshAndNotify();
        }
      } else if (pse.getValueChanged()) {
        trySaveThenRefresh();
      }
    }
  }

}
