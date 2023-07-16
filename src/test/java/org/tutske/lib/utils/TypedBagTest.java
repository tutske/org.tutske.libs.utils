package org.tutske.lib.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


class TypedBagTest {

	Key key = new Key ("key");

	@Test
	void it_should_known_when_it_contains_a_primary_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value", "secondary value");
		}};

		assertThat (bag.containsValue ("primary value"), is (true));
	}

	@Test
	void it_should_known_when_it_contains_a_secondary_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value", "secondary value");
		}};

		assertThat (bag.containsValue ("secondary value"), is (true));
	}

	@Test
	void it_should_know_when_it_does_not_contain_a_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value", "secondary value");
		}};

		assertThat (bag.containsValue ("other value"), is (false));
	}

	@Test
	void it_should_remember_the_values () {
		Bag<Key, String> bag = new Bag<> ();
		bag.add (key, "value");
		assertThat (bag, hasEntry (key, "value"));
	}

	@Test
	void it_should_remember_multi_put_values () {
		Bag<Key, String> bag = new Bag<> ();
		bag.add (key, "primary", "secondary");
		assertThat (bag.getAll (key), contains ("primary", "secondary"));
	}

	@Test
	void it_should_remember_multiple_values_on_the_same_key () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "first value");
			add (key, "second value");
		}};

		assertThat (bag, hasKey (key));
		assertThat (bag, hasEntry (key, "first value"));
		assertThat (bag.getAll (key), containsInAnyOrder ("first value", "second value"));
	}

	@Test
	void it_should_remember_multiple_values_from_lists () {
		Bag<Key, String> bag = new Bag<> ();
		bag.add (key, Arrays.asList ("first", "second"));

		assertThat (bag.getAll (key), contains ("first", "second"));
	}

	@Test
	void it_should_should_allow_adding_multiple_values_to_the_same_key_at_once () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "first value", "second value");
		}};

		assertThat (bag, hasKey (key));
		assertThat (bag, hasEntry (key, "first value"));
		assertThat (bag.getAll (key), containsInAnyOrder ("first value", "second value"));
	}

	@Test
	void it_should_not_change_the_primary_value_when_adding_on_a_key_with_a_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value");
		}};

		bag.add (key, "secondary value");

		assertThat (bag.get (key), is ("primary value"));
	}

	@Test
	void it_should_change_the_primary_value_when_putting_on_a_key_with_a_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value");
		}};

		bag.put (key, "new primary value");

		assertThat (bag.get (key), is ("new primary value"));
	}

	@Test
	void it_should_retain_the_old_primary_value_when_putting_on_a_key_with_a_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value");
		}};

		bag.put (key, "new primary value");

		assertThat (bag.getAll (key), hasItem ("old primary value"));
	}

	@Test
	void it_should_retain_the_newly_put_multiple_values () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value");
		}};

		bag.put (key, "new primary value", "new secondary value");

		assertThat (bag.getAll (key), hasItem ("new primary value"));
		assertThat (bag.getAll (key), hasItem ("new secondary value"));
	}

	@Test
	void it_should_retain_the_old_primary_value_when_putting_multiple_values_on_a_key_with_a_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value");
		}};

		bag.put (key, "new primary value", "new secondary value");

		assertThat (bag.getAll (key), hasItem ("old primary value"));
	}

	@Test
	void it_should_have_the_new_primary_value_when_replacing_on_a_key () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value");
		}};

		bag.replace (key, "new primary value");

		assertThat (bag.get (key), is ("new primary value"));
	}

	@Test
	void it_should_no_longer_have_the_old_primary_value_when_replacing_on_a_key () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value");
		}};

		bag.replace (key, "new primary value");

		assertThat (bag.getAll (key), not (hasItem ("second value")));
	}

	@Test
	void it_should_still_have_all_the_secondary_items_when_replacing_on_a_key () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value", "secondary value");
		}};

		bag.replace (key, "new primary value");

		assertThat (bag.getAll (key), hasItem ("secondary value"));
	}

	@Test
	void it_should_replace_the_primary_value_when_replacing_on_a_key_with_an_old_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value");
		}};

		bag.replace (key, "wrong old primary value", "new primary value");

		assertThat (bag.get (key), is ("old primary value"));
	}

	@Test
	void it_should_not_replace_the_primary_value_when_replacing_on_a_key_with_an_old_secondary_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value", "old secondary value");
		}};

		bag.replace (key, "old secondary value", "new secondary value");

		assertThat (bag.get (key), is ("old primary value"));
	}

	@Test
	void it_should_replace_secondary_values_when_replacing_on_a_key_with_an_old_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value", "old secondary value");
		}};

		bag.replace (key, "old secondary value", "new secondary value");

		assertThat (bag.getAll (key), hasItem ("new secondary value"));
		assertThat (bag.getAll (key), not (hasItem ("old secondary value")));
	}

	@Test
	void it_should_only_replace_when_the_old_values_match () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value", "old secondary value");
		}};

		bag.replace (key, "wrong old value", "new value");

		assertThat (bag.getAll (key), not (hasItem ("new value")));
		assertThat (bag.getAll (key), hasItem ("old primary value"));
		assertThat (bag.getAll (key), hasItem ("old secondary value"));
	}

	@Test
	void it_should_remove_the_primary_value_when_removing_on_a_key () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value", "secondary value");
		}};

		bag.remove (key);

		assertThat (bag.getAll (key), not (hasItem ("primary value")));
		assertThat (bag.get (key), is ("secondary value"));
	}

	@Test
	void it_should_return_null_when_removing_a_key_that_is_not_in_the_bag () {
		Bag<Key, String> bag = new Bag<> ();
		assertThat (bag.remove (key), nullValue ());
	}

	@Test
	void it_should_remove_a_primary_value_when_removing_on_a_key_and_old_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value", "secondary value");
		}};

		bag.remove (key, "primary value");

		assertThat (bag.getAll (key), not (hasItem ("primary value")));
	}

	@Test
	void it_should_remove_a_secondary_value_when_removing_on_a_key_and_old_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value", "secondary value");
		}};

		bag.remove (key, "secondary value");

		assertThat (bag.getAll (key), not (hasItem ("secondary value")));
	}

	@Test
	void it_should_have_a_secondary_value_when_removing_the_primary_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value", "secondary value");
		}};

		bag.remove (key, "primary value");

		assertThat (bag.get (key), is ("secondary value"));
	}

	@Test
	void it_should_clear_all_the_values_of_single_key () {
		Key odds = new Key ("odds");
		Key evens = new Key ("evens");
		Bag<Key, String> bag = new Bag<> () {{
			add (odds, "first", "third");
			add (evens, "second", "forth");
		}};

		bag.clear (evens);

		assertThat (bag, not (hasKey ("odds")));
	}

	@Test
	void it_should_put_values_from_a_map () {
		Bag<Key, String> bag = new Bag<> ();
		bag.putAll (new HashMap<> () {{
			put (key, "value");
		}});

		assertThat (bag, hasEntry (key, "value"));
	}

	@Test
	void it_should_put_values_when_adding_a_map_as_the_primary_values () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value");
		}};
		bag.putAll (new HashMap<> () {{
			put (key, "new primary value");
		}});

		assertThat (bag, hasEntry (key, "new primary value"));
	}

	@Test
	void it_should_keep_the_original_values_as_secondary_when_putting_from_a_map () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "old primary value");
		}};
		bag.putAll (new HashMap<> () {{
			put (key, "new primary value");
		}});

		assertThat (bag.getAll (key), hasItem ("new primary value"));
	}

	@Test
	void it_should_add_the_values_in_the_map_as_secondary_when_adding_them () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value");
		}};
		bag.addAll (new HashMap<> () {{
			put (key, "secondary value");
		}});

		assertThat (bag.getAll (key), hasItem ("secondary value"));
	}

	@Test
	void it_should_not_modify_the_primary_values_when_adding_values_from_a_map () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value");
		}};
		bag.addAll (new HashMap<> () {{
			put (key, "secondary value");
		}});

		assertThat (bag, hasEntry (key, "primary value"));
	}

	@Test
	void it_should_add_all_the_values_of_an_other_bag_as_secondary_values () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value", "secondary value");
		}};

		bag.addAll (new Bag<> (){{
			add (key, "extra primary", "extra secondary");
		}});

		assertThat (bag.get (key), is ("primary value"));
		assertThat (bag.getAll (key), containsInAnyOrder (
			"primary value", "secondary value", "extra primary", "extra secondary"
		));
	}

	@Test
	void it_should_no_longer_contain_a_key_if_all_values_are_removed () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "value");
		}};
		bag.remove (key);
		assertThat (bag.containsKey (key), is (false));
		assertThat (bag, not (hasKey (key)));
	}

	@Test
	void it_should_no_longer_contain_a_key_if_all_values_are_removed_with_old_values () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "value");
		}};
		bag.remove (key, "value");
		assertThat (bag.containsKey (key), is (false));
		assertThat (bag, not (hasKey (key)));
	}

	@Test
	void it_should_no_longer_contain_a_key_if_all_values_are_cleared () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "value");
		}};
		bag.clear (key);
		assertThat (bag.containsKey (key), is (false));
		assertThat (bag, not (hasKey (key)));
	}

	@Test
	void it_should_say_it_contains_primary_values () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value");
		}};
		assertThat (bag.containsValue ("primary value"), is (true));
	}

	@Test
	void it_should_say_it_contains_secondary_values () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary value", "secondary value");
		}};
		assertThat (bag.containsValue ("secondary value"), is (true));
	}

	@Test
	void it_should_know_it_has_keys_even_when_there_is_no_value () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key);
		}};

		assertThat (bag.containsKey (key), is (true));
		assertThat (bag.get (key), nullValue ());
	}

	@Test
	void it_should_forget_about_all_values () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary");
		}};

		bag.clear ();

		assertThat (bag.keySet (), hasSize (0));
		assertThat (bag.values (), hasSize (0));
		assertThat (bag.get (key), nullValue ());
	}

	@Test
	void it_should_set_values_when_iterating_over_the_entries () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary");
			add (new Key ("other"), "primary");
		}};

		for ( Map.Entry<Key, String> entry : bag.entrySet () ) {
			entry.setValue ("mask");
		}

		assertThat (bag.get (key), is ("mask"));
	}

	@Test
	void it_should_set_values_of_secondary_values_when_iterating_over_entries () {
		Bag<Key, String> bag = new Bag<> () {{
			add (key, "primary", "secondary");
		}};

		for ( Map.Entry<Key, String> entry : bag.entrySet () ) {
			if ( entry.getValue ().equals ("secondary") ) {
				entry.setValue ("mask");
			}
		}

		assertThat (bag.get (key), is ("primary"));
		assertThat (bag.getAll (key), contains ("primary", "mask"));
	}

	@Test
	void it_should_put_all_values_from_another_bag () {
		Key first = new Key ("first");
		Key second = new Key ("second");
		Key third = new Key ("third");

		Bag<Key, String> bag = new Bag<> () {{
			add (first, "original primary", "original secondary");
			add (second, "original primary", "original secondary");
		}};

		bag.putAll (new Bag<> () {{
			add (second, "new primary", "new secondary");
			add (third, "new primary", "new secondary");
		}});

		assertThat (bag.getAll (first), contains ("original primary", "original secondary"));
		assertThat (bag.getAll (second), contains ("new primary", "new secondary", "original primary", "original secondary"));
		assertThat (bag.getAll (third), contains ("new primary", "new secondary"));
	}

	@Test
	void it_should_add_all_values_from_another_bag () {
		Key first = new Key ("first");
		Key second = new Key ("second");
		Key third = new Key ("third");

		Bag<Key, String> bag = new Bag<> () {{
			add (first, "original primary", "original secondary");
			add (second, "original primary", "original secondary");
		}};

		bag.addAll (new Bag<> () {{
			add (second, "new primary", "new secondary");
			add (third, "new primary", "new secondary");
		}});

		assertThat (bag.getAll (first), contains ("original primary", "original secondary"));
		assertThat (bag.getAll (second), contains ("original primary", "original secondary", "new primary", "new secondary"));
		assertThat (bag.getAll (third), contains ("new primary", "new secondary"));
	}

	@Test
	void it_should_replace_all_values_from_another_bag () {
		Key first = new Key ("first");
		Key second = new Key ("second");
		Key third = new Key ("third");

		Bag<Key, String> bag = new Bag<> () {{
			add (first, "original primary", "original secondary");
			add (second, "original primary", "original secondary", "original ternary");
		}};

		bag.replaceAll (new Bag<> () {{
			add (second, "new primary", "new secondary");
			add (third, "new primary", "new secondary");
		}});

		assertThat (bag.getAll (first), contains ("original primary", "original secondary"));
		assertThat (bag.getAll (second), contains ("new primary", "new secondary"));
		assertThat (bag, not (hasKey (third)));
	}

	@Test
	void it_should_squash_all_values_from_another_bag_into_the_current_one () {
		Key first = new Key ("first");
		Key second = new Key ("second");
		Key third = new Key ("third");

		Bag<Key, String> bag = new Bag<> () {{
			add (first, "original primary", "original secondary");
			add (second, "original primary", "original secondary", "original ternary");
		}};

		bag.squash (new Bag<> () {{
			add (second, "new primary", "new secondary");
			add (third, "new primary", "new secondary");
		}});

		assertThat (bag.getAll (first), contains ("original primary", "original secondary"));
		assertThat (bag.getAll (second), contains ("new primary", "new secondary"));
		assertThat (bag.getAll (third), contains ("new primary", "new secondary"));
	}

	public static class Key {
		private final String n;
		public Key (String n) { this.n = n; }
		@Override public String toString () {
			return n;
		}
	}

}
