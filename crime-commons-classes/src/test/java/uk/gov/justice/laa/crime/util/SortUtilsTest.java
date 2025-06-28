package uk.gov.justice.laa.crime.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

class SortUtilsTest {

    record Person (String firstName, String lastName, int age) { }

    @Test
    void givenList_whenSortListIsInvoked_thenSortsByPrimaryAscending() {
        List<SortUtilsTest.Person> people = new ArrayList<>(List.of(
                new Person("Alice", "Smith", 30),
                new Person("Bob", "Jones", 25),
                new Person("Charlie", "Brown", 35)
        ));

        SortUtils.sortList(people, Person::firstName, false);

        assertThat(people.stream().map(Person::firstName).toList())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsExactly("Alice", "Bob", "Charlie");
    }

    @Test
    void givenList_whenSortListIsInvoked_thenSortsByPrimaryDescending() {
        List<Person> people = new ArrayList<>(List.of(
                new Person("Alice", "Smith", 30),
                new Person("Bob", "Jones", 25),
                new Person("Charlie", "Brown", 35)
        ));

        SortUtils.sortList(people, Person::firstName, true);

        assertThat(people.stream().map(Person::firstName).toList())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsExactly("Charlie", "Bob", "Alice");
    }

    @Test
    void givenList_whenSortListWithSecondaryIsInvoked_thenSortsByPrimaryThenSecondary() {
        List<Person> people = new ArrayList<>(List.of(
                new Person("Alice", "Brown", 30),
                new Person("Alice", "Anderson", 25),
                new Person("Bob", "Smith", 35)
        ));

        SortUtils.sortList(people, Person::firstName, Person::lastName, false);

        assertThat(people.stream().map(p -> p.firstName + " " + p.lastName).toList())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsExactly("Alice Anderson", "Alice Brown", "Bob Smith");
    }
}
