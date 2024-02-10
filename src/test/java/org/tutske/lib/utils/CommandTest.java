package org.tutske.lib.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CommandTest {

	@DisplayName ("Run the sub command given in the args") @Test void t_1 () throws Exception {
		Command.CommandFunction target = Mockito.mock (Command.CommandFunction.class);

		Command.run (new String [] { "target" }, Map.ofEntries (
			entry ("target", target)
		));

		verify (target, times (1)).execute (eq ("target"), any (), any ());
	}

	@DisplayName ("Run the main command if no commands are given in the args") @Test void t_2 () throws Exception {
		Command.CommandFunction target = Mockito.mock (Command.CommandFunction.class);

		Command.run (new String [] {}, Map.ofEntries (
			entry ("__main__", target)
		));

		verify (target, times (1)).execute (eq ("__main__"), any (), any ());
	}

	@DisplayName ("Run the main command if first arg is not a valid command") @Test void t_3 () throws Exception {
		Command.CommandFunction target = Mockito.mock (Command.CommandFunction.class);

		Command.run (new String [] { "not-a-cmd"}, Map.ofEntries (
			entry ("__main__", target)
		));

		verify (target, times (1)).execute (eq ("__main__"), any (), any ());
	}

	@DisplayName ("ignore casing in sub command to find a match") @Test void t_10 () throws Exception {
		Command.CommandFunction target = Mockito.mock (Command.CommandFunction.class);

		Command.run (new String [] { "CoMmAnD" }, Map.ofEntries (
			entry ("command", target)
		));

		verify (target, times (1)).execute (eq ("CoMmAnD"), any (), any ());
	}


	@DisplayName ("Run the command with the matching name") @Test void t_4 () throws Exception {
		Command.CommandFunction decoy = Mockito.mock (Command.CommandFunction.class);
		Command.CommandFunction target = Mockito.mock (Command.CommandFunction.class);

		Command.run (new String [] { "second"}, Map.ofEntries (
			entry ("first", decoy),
			entry ("second", target),
			entry ("third", decoy)
		));

		verify (target, times (1)).execute (eq ("second"), any (), any ());
		verify (decoy, times (0)).execute (any (), any (), any ());
	}

	@DisplayName ("pass the remainder of the args to the target") @Test void t_5 () throws Exception {
		List<String> memory = new ArrayList<> ();

		Command.run (new String [] { "cmd", "with", "sub", "cmds" }, Map.ofEntries (
			entry ("cmd", (cmd, opts, tail) -> { memory.addAll (Arrays.asList (tail)); })
		));

		assertThat (memory, hasSize (3));
		assertThat (memory, contains ("with", "sub", "cmds"));
	}

	@DisplayName ("pass the the whole args to the __main__ command") @Test void t_6 () throws Exception {
		List<String> memory = new ArrayList<> ();

		Command.run (new String [] { "cmd", "with", "sub", "cmds" }, Map.ofEntries (
			entry ("__main__", (cmd, opts, tail) -> { memory.addAll (Arrays.asList (tail)); })
		));

		assertThat (memory, hasSize (4));
		assertThat (memory, contains ("cmd", "with", "sub", "cmds"));
	}

	@DisplayName ("don't pass flags in the tail to the runner") @Test void t_7 () throws Exception {
		List<String> memory = new ArrayList<> ();

		Command.run (new String [] { "cmd", "--bool", "sub", "--number=3", "last" }, Map.ofEntries (
			entry ("cmd", (cmd, opts, tail) -> { memory.addAll (Arrays.asList (tail)); })
		));

		assertThat (memory, hasSize (2));
		assertThat (memory, contains ("sub", "last"));
	}

	@DisplayName ("don't pass flags in the tail to the runner") @Test void t_8 () throws Exception {
		List<String> memory = new ArrayList<> ();

		Command.run (new String [] { "cmd", "--bool", "sub", "--", "--number=3", "last" }, Map.ofEntries (
			entry ("cmd", (cmd, opts, tail) -> { memory.addAll (Arrays.asList (tail)); })
		));

		assertThat (memory, hasSize (3));
		assertThat (memory, contains ("sub", "--number=3", "last"));
	}

	@DisplayName ("pass flags in the options to the runner") @Test void t_9 () throws Exception {
		List<String> memory = new ArrayList<> ();

		Command.run (new String [] { "cmd", "--value=result" }, Map.ofEntries (
			entry ("cmd", (cmd, opts, tail) -> { memory.add (opts.option ("value")); })
		));

		assertThat (memory, hasItem ("result"));
	}

	@DisplayName ("complain when sub commands is expected but not given") @Test void t_e_4 () throws Exception {
		Exception ex = expectRunFailure (new String [] {}, Map.ofEntries (
			entry ("run", (cmd, opts, tail) -> {})
		));

		assertThat (ex.getMessage (), containsString ("invalid command"));
		assertThat (ex.getMessage (), containsString ("__main__"));
	}

	@DisplayName ("complain when sub commands is expected but an invalid one is given") @Test void t_e_5 () throws Exception {
		Exception ex = expectRunFailure (new String [] { "execute" }, Map.ofEntries (
			entry ("run", (cmd, opts, tail) -> {})
		));

		assertThat (ex.getMessage (), containsString ("invalid command"));
		assertThat (ex.getMessage (), containsString ("execute"));
	}

	@DisplayName ("complain when sub commands is expected but an invalid one is given") @Test void t_e_6 () throws Exception {
		Exception ex = expectRunFailure (new String [] { "execute" }, Map.ofEntries (
			entry ("first", (cmd, opts, tail) -> {}),
			entry ("second", (cmd, opts, tail) -> {}),
			entry ("third", (cmd, opts, tail) -> {})
		));

		assertThat (ex.getMessage (), containsString ("invalid command"));
		assertThat (ex.getMessage (), containsString ("first"));
		assertThat (ex.getMessage (), containsString ("second"));
		assertThat (ex.getMessage (), containsString ("third"));
	}

	@DisplayName ("complain when sub commands is expected but an invalid one is given") @Test void t_s_1 () throws Exception {
		Command.CommandFunction target = Mockito.mock (Command.CommandFunction.class);

		Command.run (new String [] { "sub", "target" }, Map.ofEntries (
			entry ("sub", (cmd, opts, tail) -> {
				Command.run (opts, tail, Map.ofEntries (
					entry ("target", target)
				));
			})
		));

		verify (target, times (1)).execute (eq ("target"), any (), any ());
	}

	public RuntimeException expectRunFailure (String [] args, Map<String, Command.CommandFunction> fns) {
		return assertThrows (RuntimeException.class, () -> Command.run (args, fns));
	}

}
