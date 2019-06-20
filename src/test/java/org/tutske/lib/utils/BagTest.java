package org.tutske.lib.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class BagTest {

	@Test
	public void it_should_know_when_it_does_not_contain_any_values () {
		assertThat (new Bag<String, String> ().isEmpty (), is (true));
	}

	@Test
	public void it_should_know_the_number_of_keys_in_the_bag () {
		Bag<String, String> bag = new Bag<> ();
		bag.add ("key", "value");
		assertThat (bag.size (), is (1));
	}

	@Test
	public void it_should_not_count_multiple_values_of_the_same_key_for_size () {
		Bag<String, String> bag = new Bag<> ();
		bag.add ("key", "value");
		bag.add ("key", "second");
		assertThat (bag.size (), is (1));
	}

	@Test
	public void it_should_known_when_it_does_not_contain_a_key () {
		assertThat (new Bag<String, String> ().containsKey ("key"), is (false));
	}

	@Test
	public void it_should_known_when_it_does_contain_a_key_after_put () {
		Bag<String, String> bag = new Bag<> ();
		bag.put ("key", "value");
		assertThat (bag.containsKey ("key"), is (true));
	}

	@Test
	public void it_should_known_when_it_does_contain_a_key_after_add () {
		Bag<String, String> bag = new Bag<> ();
		bag.add ("key", "value");
		assertThat (bag.containsKey ("key"), is (true));
	}

	@Test
	public void it_should_replace_the_values () {
		Bag<String, String> bag = new Bag<> ();
		bag.add ("key", "value");
		bag.replace ("key", "value", "new");

		assertThat (bag.getAll ("key"), contains ("new"));
		assertThat (bag.getAll ("key"), hasItem ("new"));
	}

	@Test
	public void it_should_remember_the_values () {
		Bag<String, String> bag = new Bag<> ();
		bag.add ("key", "value");
		assertThat (bag, hasEntry ("key", "value"));
	}

	@Test
	public void it_should_remember_multiple_values_on_the_same_key () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "first value");
			add ("key", "second value");
		}};

		assertThat (bag, hasKey ("key"));
		assertThat (bag, hasEntry ("key", "first value"));
		assertThat (bag.getAll ("key"), containsInAnyOrder ("first value", "second value"));
	}

	@Test
	public void it_should_should_allow_adding_multiple_values_to_the_same_key_at_once () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "first value", "second value");
		}};

		assertThat (bag, hasKey ("key"));
		assertThat (bag, hasEntry ("key", "first value"));
		assertThat (bag.getAll ("key"), containsInAnyOrder ("first value", "second value"));
	}

	@Test
	public void it_should_should_allow_adding_multiple_values_to_the_same_key_as_a_list () {
		List<String> values = new LinkedList<> ();
		values.add ("first value");
		values.add ("second value");

		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", values);
		}};

		assertThat (bag, hasKey ("key"));
		assertThat (bag, hasEntry ("key", "first value"));
		assertThat (bag.getAll ("key"), containsInAnyOrder ("first value", "second value"));
	}

	@Test
	public void it_should_not_change_the_primary_value_when_adding_on_a_key_with_a_value () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value");
		}};

		bag.add ("key", "secondary value");

		assertThat (bag.get ("key"), is ("primary value"));
	}

	@Test
	public void it_should_change_the_primary_value_when_putting_on_a_key_with_a_value () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value");
		}};

		bag.put ("key", "new primary value");

		assertThat (bag.get ("key"), is ("new primary value"));
	}

	@Test
	public void it_should_retain_the_old_primary_value_when_putting_on_a_key_with_a_value () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "old primary value");
		}};

		bag.put ("key", "new primary value");

		assertThat (bag.getAll ("key"), hasItem ("old primary value"));
	}

	@Test
	public void it_should_have_the_new_primary_value_when_replacing_on_a_key () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "old primary value");
		}};

		bag.replace ("key", "new primary value");

		assertThat (bag.get ("key"), is ("new primary value"));
	}

	@Test
	public void it_should_no_longer_have_the_old_primary_value_when_replacing_on_a_key () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "old primary value");
		}};

		bag.replace ("key", "new primary value");

		assertThat (bag.getAll ("key"), not (hasItem ("second value")));
	}

	@Test
	public void it_should_still_have_all_the_secondary_items_when_replacing_on_a_key () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "old primary value", "secondary value");
		}};

		bag.replace ("key", "new primary value");

		assertThat (bag.getAll ("key"), hasItem ("secondary value"));
	}

	@Test
	public void it_should_replace_the_primary_value_when_replacing_on_a_key_with_an_old_value () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "old primary value");
		}};

		bag.replace ("key", "wrong old primary value", "new primary value");

		assertThat (bag.get ("key"), is ("old primary value"));
	}

	@Test
	public void it_should_not_replace_the_primary_value_when_replacing_on_a_key_with_an_old_secondary_value () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "old primary value", "old secondary value");
		}};

		bag.replace ("key", "old secondary value", "new secondary value");

		assertThat (bag.get ("key"), is ("old primary value"));
	}

	@Test
	public void it_should_replace_secondary_values_when_replacing_on_a_key_with_an_old_value () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "old primary value", "old secondary value");
		}};

		bag.replace ("key", "old secondary value", "new secondary value");

		assertThat (bag.getAll ("key"), hasItem ("new secondary value"));
		assertThat (bag.getAll ("key"), not (hasItem ("old secondary value")));
	}

	@Test
	public void it_should_only_replace_when_the_old_values_match () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "old primary value", "old secondary value");
		}};

		bag.replace ("key", "wrong old value", "new value");

		assertThat (bag.getAll ("key"), not (hasItem ("new value")));
		assertThat (bag.getAll ("key"), hasItem ("old primary value"));
		assertThat (bag.getAll ("key"), hasItem ("old secondary value"));
	}

	@Test
	public void it_should_remove_the_primary_value_when_removing_on_a_key () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value", "secondary value");
		}};

		bag.remove ("key");

		assertThat (bag.getAll ("key"), not (hasItem ("primary value")));
		assertThat (bag.get ("key"), is ("secondary value"));
	}

	@Test
	public void it_should_not_remove_anything_if_the_value_is_null () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value", "secondary value");
		}};

		bag.remove ("key", null);

		assertThat (bag.get ("key"), is ("primary value"));
		assertThat (bag.getAll ("key"), hasItem ("secondary value"));
	}

	@Test
	public void it_should_not_remove_anything_if_the_value_is_not_present () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value", "secondary value");
		}};

		bag.remove ("key", "ternary value");

		assertThat (bag.get ("key"), is ("primary value"));
		assertThat (bag.getAll ("key"), hasItem ("secondary value"));
	}

	@Test
	public void it_should_not_remove_anything_if_the_key_is_not_present () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value", "secondary value");
		}};

		bag.remove ("other", "primary value");

		assertThat (bag.get ("key"), is ("primary value"));
		assertThat (bag.getAll ("key"), hasItem ("secondary value"));
	}

	@Test
	public void it_should_return_null_when_removing_a_key_that_is_not_in_the_bag () {
		Bag<String, String> bag = new Bag<> ();
		assertThat (bag.remove ("key"), nullValue ());
	}

	@Test
	public void it_should_remove_a_primary_value_when_removing_on_a_key_and_old_value () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value", "secondary value");
		}};

		bag.remove ("key", "primary value");

		assertThat (bag.getAll ("key"), not (hasItem ("primary value")));
	}

	@Test
	public void it_should_remove_a_secondary_value_when_removing_on_a_key_and_old_value () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value", "secondary value");
		}};

		bag.remove ("key", "secondary value");

		assertThat (bag.getAll ("key"), not (hasItem ("secondary value")));
	}

	@Test
	public void it_should_have_a_secondary_value_when_removing_the_primary_value () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value", "secondary value");
		}};

		bag.remove ("key", "primary value");

		assertThat (bag.get ("key"), is ("secondary value"));
	}

	@Test
	public void it_should_clear_all_the_values_of_single_key () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("odds", "first", "third");
			add ("evens", "second", "forth");
		}};

		bag.clear ("odds");

		assertThat (bag, not (hasKey ("odds")));
	}

	@Test
	public void it_should_put_values_from_a_map () {
		Bag<String, String> bag = new Bag<> ();
		bag.putAll (new HashMap<String, String> () {{
			put ("key", "value");
		}});

		assertThat (bag, hasEntry ("key", "value"));
	}

	@Test
	public void it_should_put_values_when_adding_a_map_as_the_primary_values () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "old primary value");
		}};
		bag.putAll (new HashMap<String, String> () {{
			put ("key", "new primary value");
		}});

		assertThat (bag, hasEntry ("key", "new primary value"));
	}

	@Test
	public void it_should_keep_the_original_values_as_secondary_when_putting_from_a_map () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "old primary value");
		}};
		bag.putAll (new HashMap<String, String> () {{
			put ("key", "new primary value");
		}});

		assertThat (bag.getAll ("key"), hasItem ("new primary value"));
	}

	@Test
	public void it_should_add_the_values_in_the_map_as_secondary_when_adding_them () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value");
		}};
		bag.addAll (new HashMap<String, String> () {{
			put ("key", "secondary value");
		}});

		assertThat (bag.getAll ("key"), hasItem ("secondary value"));
	}

	@Test
	public void it_should_not_modify_the_primary_values_when_adding_values_from_a_map () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value");
		}};
		bag.addAll (new HashMap<String, String> () {{
			put ("key", "secondary value");
		}});

		assertThat (bag, hasEntry ("key", "primary value"));
	}

	@Test
	public void it_should_add_all_the_values_of_an_other_bag_as_secondary_values () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value", "secondary value");
		}};

		bag.addAll (new Bag<String, String> (){{
			add ("key", "extra primary", "extra secondary");
		}});

		assertThat (bag.get ("key"), is ("primary value"));
		assertThat (bag.getAll ("key"), containsInAnyOrder (
			"primary value", "secondary value", "extra primary", "extra secondary"
		));
	}

	@Test
	public void it_should_no_longer_contain_a_key_if_all_values_are_removed () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "value");
		}};
		bag.remove ("key");
		assertThat (bag.containsKey ("key"), is (false));
		assertThat (bag, not (hasKey ("key")));
	}

	@Test
	public void it_should_no_longer_contain_a_key_if_all_values_are_removed_with_old_values () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "value");
		}};
		bag.remove ("key", "value");
		assertThat (bag.containsKey ("key"), is (false));
		assertThat (bag, not (hasKey ("key")));
	}

	@Test
	public void it_should_no_longer_contain_a_key_if_all_values_are_cleared () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "value");
		}};
		bag.clear ("key");
		assertThat (bag.containsKey ("key"), is (false));
		assertThat (bag, not (hasKey ("key")));
	}

	@Test
	public void it_should_say_it_contains_primary_values () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value");
		}};
		assertThat (bag.containsValue ("primary value"), is (true));
	}

	@Test
	public void it_should_say_it_contains_secondary_values () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "primary value", "secondary value");
		}};
		assertThat (bag.containsValue ("secondary value"), is (true));
	}

	@Test
	public void it_should_parse_string_values_to_integers () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "1");
		}};

		assertThat (bag.getAs ("key", Integer.class), is (1));
	}

	@Test
	public void it_should_parse_string_values_to_longs () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "1");
		}};

		assertThat (bag.getAs ("key", Long.class), is (1L));
	}

	@Test
	public void it_should_parse_string_values_to_floats () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "1");
		}};

		assertThat (bag.getAs ("key", Float.class), is (1F));
	}

	@Test
	public void it_should_parse_string_values_to_floats_with_decimal_part () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "1.2");
		}};

		assertThat (bag.getAs ("key", Float.class), is (1.2F));
	}

	@Test
	public void it_should_parse_string_values_to_booleans () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "false");
		}};

		assertThat (bag.getAs ("key", Boolean.class), is (false));
	}

	@Test (expected = RuntimeException.class)
	public void it_should_complain_when_parsing_of_values_fails () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "one");
		}};

		assertThat (bag.getAs ("key", Integer.class), is (false));
	}

	@Test (expected = RuntimeException.class)
	public void it_should_complain_when_asking_to_convert_to_an_unknown_class () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key", "one");
		}};

		assertThat (bag.getAs ("key", BagTest.class), is (false));
	}

	@Test
	public void it_should_know_it_has_keys_even_when_there_is_no_value () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("key");
		}};

		assertThat (bag.containsKey ("key"), is (true));
		assertThat (bag.get ("key"), nullValue ());
	}

	@Test
	public void it_should_list_all_the_primary_and_secondary_values () {
		Bag<String, String> bag = new Bag<String, String> () {{
			add ("a", "a1", "a2", "a3");
			add ("b", "b1", "b2", "b3", "b4");
		}};

		assertThat (bag.values (), contains ("a1", "a2", "a3", "b1", "b2", "b3", "b4"));
	}

}
