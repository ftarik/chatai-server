package fr.fgroup.chatai.configs;

import com.github.slugify.Slugify;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 *
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 *
 * Created 17/06/2020 17:12
 */

@Configuration
public class BeanProviderConfig {

  @Bean
  public Slugify getSlugify() {
    return new Slugify();
  }

}
