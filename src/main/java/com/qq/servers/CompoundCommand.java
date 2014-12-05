package com.qq.servers;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.qq.servers.tfidfproducer.Fragment;
import org.codehaus.jackson.node.ObjectNode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Rao
 * Date: 13-11-10
 * Time: 下午3:23
 */
public class CompoundCommand implements Command
{
    private static Comparator<Command> comparator = new Comparator<Command>()
    {
        @Override
        public int compare(Command command1, Command command2)
        {
            return Ints.compare(command1.getPreference(), command2.getPreference());
        }
    };

    private List<Command> commands;

    public CompoundCommand(List<Command> commands)
    {
        this.commands = commands;
    }

    @Override
    public String getName()
    {
        return Opcode.COMPOUND.getName();
    }

    @Override
    public boolean getAnnotation()
    {
        boolean annotation = false;
        for (Command command : commands)
        {
            annotation |= command.getAnnotation();
        }
        return annotation;
    }

    @Override
    public int getPreference()
    {
        return -1;
    }

    @Override
    public List<Fragment> getFragments(boolean annotation)
    {
        Collections.sort(commands, comparator);
        return commands.get(0).getFragments(annotation);
    }

    @Override
    public void execute(List<Fragment> fragments, ObjectNode result)
    {
        for (Command command : commands)
        {
            command.execute(fragments, result);
        }
    }

    public static CompoundCommand createCommand(Map<String, List<String>> parameters)
    {
        String value = parameters.get(Command.OP_CODE_KEY).get(0);
        String[] strOpcodes = value.split(",");
        List<Command> commands = Lists.newArrayList();

        for (String strOpcode : strOpcodes)
        {
            Command command = CommandFactory.createSingleCommand(strOpcode, parameters);
            if (command == null)
            {
                break;
            }
            commands.add(command);
//            System.out.println(command.getName());

        }
        return new CompoundCommand(commands);
    }
}
