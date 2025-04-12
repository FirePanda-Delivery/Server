package ru.diplom.fpd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import jakarta.persistence.*;
import java.util.List;
import java.util.Locale;

@Entity
@Data
public class Cities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonValue
    @Column
    private String citi;

    @Column
    @JsonIgnore
    private String normalizedCiti;

    public void setCiti(String citi) {
        this.citi = citi;
        normalizedCiti = citi.toUpperCase(Locale.ROOT);
    }
//    @ElementCollection
//    @CollectionTable()

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(nullable = false, unique = true)
    @JoinColumn(name = "cityId")
    private List<CitiesCoordinates> Cords;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cities cities = (Cities) o;

        if (cities.citi == null || cities.normalizedCiti == null) {
            return false;
        }

        return cities.citi.equals(this.citi) && cities.normalizedCiti.equals(this.normalizedCiti);
    }

    @Override
    public int hashCode() {
        int result = citi != null ? citi.hashCode() : 0;
        result = 31 * result + (normalizedCiti != null ? normalizedCiti.hashCode() : 0);
        return result;
    }
}
