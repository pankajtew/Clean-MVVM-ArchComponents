package com.k0d4black.theforce.viewmodels

import com.google.common.truth.Truth
import com.k0d4black.theforce.BaseViewModelTest
import com.k0d4black.theforce.domain.usecases.CharacterSearchUseCase
import com.k0d4black.theforce.features.character_search.CharacterSearchViewModel
import com.k0d4black.theforce.mappers.toPresentation
import com.k0d4black.theforce.utils.SampleData
import com.k0d4black.theforce.utils.observeOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class CharacterSearchViewModelTest : BaseViewModelTest() {

    @Mock
    lateinit var characterSearchUseCase: CharacterSearchUseCase

    private lateinit var characterSearchViewModel: CharacterSearchViewModel

    private val searchParams = "Darth"

    @Before
    fun setup() {
        characterSearchViewModel = CharacterSearchViewModel(characterSearchUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun shouldReceiveSearchResults() {
        runBlockingTest {
            setMockAnswer()
            characterSearchViewModel.executeCharacterSearch(searchParams)
            characterSearchViewModel.searchResults.observeOnce {
                Truth.assertThat(it).isEqualTo(SampleData.searchResults.map { it.toPresentation() })

            }
        }
    }

    private suspend fun setMockAnswer() {
        given(characterSearchUseCase.execute(searchParams)).willReturn(flow {
            emit(SampleData.searchResults)
        })
    }
}