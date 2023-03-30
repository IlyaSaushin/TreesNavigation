package com.earl.treesnavigation.di

import android.content.Context
import com.earl.treesnavigation.data.BaseRepository
import com.earl.treesnavigation.data.localDataSource.AppDatabase
import com.earl.treesnavigation.data.localDataSource.NodesDao
import com.earl.treesnavigation.data.localDataSource.createDatabase
import com.earl.treesnavigation.data.localDataSource.enteties.NodeDb
import com.earl.treesnavigation.data.mappers.NodeDbToMainMapper
import com.earl.treesnavigation.domain.Interactor
import com.earl.treesnavigation.domain.Repository
import com.earl.treesnavigation.domain.mappers.ChildNodeToDbMapper
import com.earl.treesnavigation.domain.models.ChildNode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ) = createDatabase(app)

    @Singleton
    @Provides
    fun provideNodesDao(appDb: AppDatabase) = appDb.nodesDao()

    @Singleton
    @Provides
    fun provideRepository(
        dao: NodesDao,
        childNodeToDbMapper: ChildNodeToDbMapper<NodeDb>,
        nodeDbToMainMapper: NodeDbToMainMapper<ChildNode>
    ) : Repository {
        return BaseRepository(
            dao,
            childNodeToDbMapper,
            nodeDbToMainMapper
        )
    }

    @Singleton
    @Provides
    fun provideInteractor(
        repository: Repository
    ) : Interactor {
        return Interactor.Base(
            repository
        )
    }
}