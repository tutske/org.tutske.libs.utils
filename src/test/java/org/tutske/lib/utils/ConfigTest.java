package org.tutske.lib.utils;

import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;


public class ConfigTest {

	@DisplayName ("A config container with no providers loaded") @Nested public class EmptyConfigTest {

		Config config = new Config ();

		@DisplayName ("does not not contain any values") @Test void t_1 () throws Exception {
			assertThat (config.has ("option-key"), is (false));
		}

		@DisplayName ("produces a null when getting any option value") @Test void t_2 () throws Exception {
			String value = config.option ("option-key");
			assertThat (value, nullValue ());
		}

		@DisplayName ("produces a default when getting any option value") @Test void t_3 () throws Exception {
			String value = config.optionOrDefault ("option-key", "default");
			assertThat (value, is ("default"));
		}

		@DisplayName ("uses nulls while computing any option value") @Test void t_4 () throws Exception {
			String value = config.compute (c -> c.option ("option-key"));
			assertThat (value, nullValue ());
		}

		@DisplayName ("uses defaults while computing any option value") @Test void t_5 () throws Exception {
			String value = config.compute (c -> c.optionOrDefault ("option-key", "default"));
			assertThat (value, is ("default"));
		}

		@DisplayName ("does not apply any transformations while getting any option value") @Test void t_6 () throws Exception {
			Function<String, Object> transform = mock (Function.class);

			Object value = config.option ("option-key", transform);

			assertThat (value, nullValue ());
			verify (transform, times (0)).apply (any ());
		}

	}

	@DisplayName ("A config container with a map provider loaded") @Nested public class MapConfig {

		Config config = new Config ().load (10, Config.mapProvider (Map.of (
			"first-option", "first-value",
			"second-option", "second-value"
		)));

		@DisplayName ("contains options present in the map") @Test void t_1 () throws Exception {
			assertThat (config.has ("first-option"), is (true));
			assertThat (config.has ("second-option"), is (true));
			assertThat (config.has ("non-existing"), is (false));
		}

	}

	@DisplayName ("A config container with a map provider loaded") @Nested public class ArgumentOConfig {

		@DisplayName ("retrieves the value supplied after the equal sign") @Test void t_1 () throws Exception {
			Config config = instance ("--option-key=option-value");
			String value = config.optionOrDefault ("option-key", "default");
			assertThat (value, is ("option-value"));
		}

		@DisplayName ("retrieves the value supplied after the equal sign") @Test void t_1_1 () throws Exception {
			Config config = instance ("--option-key");
			String value = config.optionOrDefault ("option-key", "default");
			assertThat (value, is (""));
		}

		@DisplayName ("transforms options without values with an empty string") @Test void t_1_2 () throws Exception {
			Function<String, Object> transform = mock (Function.class);

			Config config = instance ("--option-key");
			config.option ("option-key", transform);

			verify (transform).apply ("");
		}

		@DisplayName ("retrieves the value supplied after the equal sign") @Test void t_2 () throws Exception {
			Config config = instance ("--option-key=option=value");
			String value = config.optionOrDefault ("option-key", "default");
			assertThat (value, is ("option=value"));
		}

		@DisplayName ("does not consider any arguments after a '--'") @Test void t_3 () throws Exception {
			Config config = instance ("--", "--option-key=option-value");
			assertThat (config.has ("option-key"), is (false));
		}

		@DisplayName ("continues parsing options after positional arguments are found") @Test void t_4 () throws Exception {
			Config config = instance ("--one=1", "two", "--three=3", "-four=false", "--five=5", "--", "--not-an=option");

			assertThat (config.has ("one"), is (true));
			assertThat (config.has ("three"), is (true));
			assertThat (config.has ("five"), is (true));

			assertThat (config.has ("two"), is (false));
			assertThat (config.has ("four"), is (false));
			assertThat (config.has ("not-an"), is (false));
		}

		private Config instance (String ... args) {
			return new Config ().load (10, Config.argumentsProvider (args));
		}

	}

	@DisplayName ("extracting the tail ignores option flags") @Test void t_4 () throws Exception {
		String [] tail = Config.extractTail ("cmd", "--some-option");
		assertThat (tail.length, is (1));
		assertThat (tail[0], is ("cmd"));
	}

	@DisplayName ("extracting the tail includes option flags after a '--'") @Test void t_5 () throws Exception {
		String [] tail = Config.extractTail ("cmd", "--", "--some-option");
		assertThat (tail.length, is (2));
		assertThat (tail[0], is ("cmd"));
		assertThat (tail[1], is ("--some-option"));
	}

	@DisplayName ("extracting the tail includes arguments after some options") @Test void t_6 () throws Exception {
		String [] tail = Config.extractTail ("cmd", "--some-option", "sub");
		assertThat (tail.length, is (2));
		assertThat (tail[0], is ("cmd"));
		assertThat (tail[1], is ("sub"));
	}

}
