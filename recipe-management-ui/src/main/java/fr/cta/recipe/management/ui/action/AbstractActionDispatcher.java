package fr.cta.recipe.management.ui.action;

import java.util.*;
import java.util.function.Function;

public abstract class AbstractActionDispatcher<STATE, ACTION> {

    private STATE state;

    private final Map<String, StateChangeListener<STATE, ACTION>> stateChangeListeners = new HashMap<>();

    protected abstract STATE initiateState();
    protected abstract STATE doDispatchAction(STATE state, ACTION action);

    protected AbstractActionDispatcher() {
        state = initiateState();
        validateState(state);
    }

    public final void dispatchAction(ACTION action) {
        STATE newState = doDispatchAction(state, action);
        validateState(newState);

        if (shouldFireChangeEvent(newState)) {
            STATE oldState = state;
            state = newState;
            fireStateChangeEvent(new StateChangeEvent<>(newState, oldState, action));
        }
    }

    protected boolean shouldFireChangeEvent(STATE newState) {
        return !state.equals(newState);
    }

    public Registration addStateChangeListener(StateChangeListener<STATE, ACTION> stateStateChangeListener) {
        return addStateChangeListener(stateStateChangeListener.getClass().getSimpleName(), stateStateChangeListener);
    }

    public Registration addStateChangeListener(String listenerKey, StateChangeListener<STATE, ACTION> stateStateChangeListener) {
        stateChangeListeners.put(listenerKey, stateStateChangeListener);
        return () -> removeStateChangeListener(listenerKey);
    }

    public void removeStateChangeListener(StateChangeListener<STATE, ACTION> stateStateChangeListener) {
        removeStateChangeListener(stateStateChangeListener.getClass().getSimpleName());
    }

    public void removeStateChangeListener(String listenerKey) {
        stateChangeListeners.remove(listenerKey);
    }

    private void fireStateChangeEvent(StateChangeEvent<STATE, ACTION> stateChangeEvent) {
        stateChangeListeners.values().forEach(stateChangeListener -> stateChangeListener.onStateChanged(stateChangeEvent));
    }

    private void validateState(STATE state) {
        if (state == null) {
            throw new IllegalStateException("State must not be null");
        }
    }

    public STATE getState() {
        return state;
    }

    public interface StateChangeListener<STATE, ACTION> {
        void onStateChanged(StateChangeEvent<STATE, ACTION> stateChangeEvent);
    }

    public static record StateChangeEvent<STATE, ACTION>(STATE newState, STATE oldState, ACTION origin) {

        public <PROPERTY> boolean propertyChanged(Function<STATE, PROPERTY> propertyExtractor) {
            return !Objects.equals(propertyExtractor.apply(newState), propertyExtractor.apply(oldState));
        }
        public <PROPERTY> boolean hasSameProperty(Function<STATE, PROPERTY> propertyExtractor) {
            return propertyExtractor.apply(newState) == propertyExtractor.apply(oldState);
        }

        @SafeVarargs
        public final boolean hasForOriginAnyOf(Class<? extends ACTION>... actionClasses) {
            return Arrays.stream(actionClasses).anyMatch(actionClass -> origin.getClass().isAssignableFrom(actionClass));
        }

        @SafeVarargs
        public final boolean hasNotForOriginAnyOf(Class<? extends ACTION>... actionClasses) {
            return !hasForOriginAnyOf(actionClasses);
        }
    }

    public interface Registration {
        void stopListening() throws RuntimeException;
    }
}
