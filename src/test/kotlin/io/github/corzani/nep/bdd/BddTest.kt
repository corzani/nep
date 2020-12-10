package io.github.corzani.nep.bdd

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE
import org.junit.runner.RunWith

@RunWith(Cucumber::class)

@CucumberOptions(plugin = ["pretty", "summary"])
class BddTest