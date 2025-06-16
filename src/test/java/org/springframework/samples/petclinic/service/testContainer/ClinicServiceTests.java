package org.springframework.samples.petclinic.service.testContainer;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"spring-data-jpa", "postgres"})
class ClinicServiceTests extends PostgreSQLContainerSetup {

    @Autowired
    EntityManager entityManager;

    @Autowired
    private ClinicService clinicService;

    @Test
    void shouldFindPetTypeById() {
        PetType petType = this.clinicService.findPetTypeById(1);
        assertThat(petType.getName()).isEqualTo("cat");
    }

    @Test
    void shouldFindAllPetTypes() {
        Collection<PetType> petTypes = this.clinicService.findAllPetTypes();
        PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
        assertThat(petType1.getName()).isEqualTo("cat");
        PetType petType3 = EntityUtils.getById(petTypes, PetType.class, 3);
        assertThat(petType3.getName()).isEqualTo("lizard");
    }

    @Test
    @Transactional
    void shouldInsertPetType() {
        Collection<PetType> petTypes = this.clinicService.findAllPetTypes();
        int found = petTypes.size();

        PetType petType = new PetType();
        petType.setName("tiger");

        this.clinicService.savePetType(petType);
        assertThat(petType.getId().longValue()).isNotEqualTo(0);

        petTypes = this.clinicService.findAllPetTypes();
        assertThat(petTypes.size()).isEqualTo(found + 1);
    }

    @Test
    @Transactional
    void shouldUpdatePetType() {
        PetType petType = this.clinicService.findPetTypeById(1);
        String oldLastName = petType.getName();
        String newLastName = oldLastName + "X";
        petType.setName(newLastName);
        this.clinicService.savePetType(petType);
        petType = this.clinicService.findPetTypeById(1);
        assertThat(petType.getName()).isEqualTo(newLastName);
    }

    @Test
    @Transactional
    void shouldDeletePetType() {
        PetType petType = this.clinicService.findPetTypeById(1);
        this.clinicService.deletePetType(petType);
        clearCache();
        try {
            petType = this.clinicService.findPetTypeById(1);
        } catch (Exception e) {
            petType = null;
        }
        assertThat(petType).isNull();
    }

    private void clearCache() {
        entityManager.clear();
    }
}
