package com.lsc.ctesterfx.background;

/**
 * Runnable to be used to notify that the pause dialog has been closed.
 *
 * @author dma@logossmartcard.com
 */
public interface PauseNotifier extends Runnable
{
    @Override
    public void run();
}
