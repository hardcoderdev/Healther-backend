package hardcoder.dev.healtherbackend.di

import hardcoder.dev.healtherbackend.repository.advices.AdvicesRepository
import hardcoder.dev.healtherbackend.repository.advices.AdvicesRepositoryImpl
import hardcoder.dev.healtherbackend.repository.pages.PagesRepository
import hardcoder.dev.healtherbackend.repository.pages.PagesRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    provideRepositories()
}

private fun Module.provideRepositories() {
    single<AdvicesRepository> {
        AdvicesRepositoryImpl()
    }
    single<PagesRepository> {
        PagesRepositoryImpl()
    }
}
