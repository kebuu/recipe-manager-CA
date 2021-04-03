package fr.cta.recipe.management.ui;

import java.util.*;
import java.util.function.Function;

public abstract class ActionDispatcher<STATE, ACTION> {

    private STATE state;

    private final Set<StateChangeListener<STATE>> stateChangeListeners = new HashSet<>();

    protected abstract STATE initiateState();
    protected abstract STATE doDispatchAction(STATE state, ACTION action);

    protected ActionDispatcher() {
        state = initiateState();
        validateState(state);
    }

    public final void dispatchAction(ACTION action) {
        STATE newState = doDispatchAction(state, action);
        validateState(newState);

        if (!state.equals(newState)) {
            STATE oldState = state;
            state = newState;
            fireStateChangeEvent(new StateChangeEvent<>(newState, oldState));
        }
    }

    public void addStateChangeListener(StateChangeListener<STATE> stateStateChangeListener) {
        stateChangeListeners.add(stateStateChangeListener);
    }

    public void removeStateChangeListener(StateChangeListener<STATE> stateStateChangeListener) {
        stateChangeListeners.remove(stateStateChangeListener);
    }

    private void fireStateChangeEvent(StateChangeEvent<STATE> stateChangeEvent) {
        stateChangeListeners.forEach(stateChangeListener -> stateChangeListener.onStateChanged(stateChangeEvent));
    }

    private void validateState(STATE state) {
        if (state == null) {
            throw new IllegalStateException("State must not be null");
        }
    }

    public STATE getState() {
        return state;
    }

    public interface StateChangeListener<STATE> {
        void onStateChanged(StateChangeEvent<STATE> stateChangeEvent);
    }

    public static record StateChangeEvent<STATE>(STATE newState, STATE oldState) {
        public <PROPERTY> boolean propertyChanged(Function<STATE, PROPERTY> propertyExtractor) {
            return !Objects.equals(propertyExtractor.apply(newState), propertyExtractor.apply(oldState));
        }
    }
}
