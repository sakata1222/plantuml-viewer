package jp.gr.java_conf.saka.plantuml.viewer.repository.impl;

import java.util.Map;
import java.util.Optional;
import jp.gr.java_conf.saka.plantuml.viewer.repository.IPlantUmlSvgRepository;
import jp.gr.java_conf.saka.plantuml.viewer.repository.entity.PlantUmlEntity;
import org.springframework.stereotype.Repository;

@Repository
public class PlantUmlSvgRepositoryImpl implements IPlantUmlSvgRepository {

  private PlantUmlInMemoryRepository repository;

  public PlantUmlSvgRepositoryImpl(
    PlantUmlInMemoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public PlantUmlEntity create(
    PlantUmlEntity umlEntity) {
    return repository.create(umlEntity);
  }

  @Override
  public PlantUmlEntity update(
    PlantUmlEntity umlEntity) {
    return repository.update(umlEntity);
  }

  @Override
  public Map<String, PlantUmlEntity> findAll() {
    return repository.findAll();
  }

  @Override
  public Optional<PlantUmlEntity> find(String id) {
    return repository.find(id);
  }
}
