package io.github.microsphere.tools.attach;

import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * {@link VirtualMachine} Callback
 *
 * @param <T>
 *         the type of execution callback
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @version 1.0.0
 * @see VirtualMachine
 * @since 1.0.0
 */
public interface VirtualMachineCallback<V extends VirtualMachine, T> {


    /**
     * Get called by {@link VirtualMachineTemplate#execute(VirtualMachineCallback)}
     *
     * @param virtualMachine
     *         {@link VirtualMachine}
     * @return execution result
     * @throws IOException
     *         If It's failed to execute some command on virtualMachine , {@link IOException} will be thrown.
     */
    T doInVirtualMachine(V virtualMachine) throws IOException;
}
