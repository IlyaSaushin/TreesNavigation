package com.earl.treesnavigation.di

import com.earl.treesnavigation.data.localDataSource.enteties.NodeDb
import com.earl.treesnavigation.data.mappers.BaseChildNodeToDbMapper
import com.earl.treesnavigation.data.mappers.BaseNodeDbToMainMapper
import com.earl.treesnavigation.data.mappers.NodeDbToMainMapper
import com.earl.treesnavigation.domain.mappers.ChildNodeToDbMapper
import com.earl.treesnavigation.domain.models.ChildNode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MappersModule {

    @Singleton
    @Provides
    fun provideNodeDbToMainMapper() : NodeDbToMainMapper<ChildNode> {
        return BaseNodeDbToMainMapper()
    }

    @Singleton
    @Provides
    fun provideChildNodeToDbMapper() : ChildNodeToDbMapper<NodeDb> {
        return BaseChildNodeToDbMapper()
    }
}